package com.mikhailkarpov.products.service;

import com.mikhailkarpov.products.dto.ProductDto;
import com.mikhailkarpov.products.entity.Category;
import com.mikhailkarpov.products.entity.Product;
import com.mikhailkarpov.products.exception.ResourceAlreadyExistsException;
import com.mikhailkarpov.products.exception.ResourceNotFoundException;
import com.mikhailkarpov.products.repository.CategoryRepository;
import com.mikhailkarpov.products.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public ProductDto createProduct(Long categoryId, ProductDto product) {

        String code = product.getCode();

        if (productRepository.existsByCode(code)) {
            String message = String.format("Product '{}' already exists", code);
            throw new ResourceAlreadyExistsException(message);
        }

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> {
            String message = String.format("Category with id '%d' not found");
            return new ResourceNotFoundException(message);
        });

        Product created = productRepository.save(Product.builder()
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .amount(product.getAmount())
                .category(category)
                .build());
        log.info("Creating {}", created);

        return product;
    }

    @Override
    @Transactional
    public ProductDto updateProduct(String code, ProductDto update) {
        Product product = getProductByCodeOrElseThrow(code);

        product.setName(update.getName());
        product.setDescription(update.getDescription());
        product.setAmount(update.getAmount());
        log.info("Updating {}", product);

        return update;
    }

    private Product getProductByCodeOrElseThrow(String code) {
        Optional<Product> product = productRepository.findByCode(code);

        if (!product.isPresent()) {
            String message = String.format("Product with code={} not found", code);
            throw new ResourceNotFoundException(message);
        }

        return product.get();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto findProductByCode(String code) {

        Product product = getProductByCodeOrElseThrow(code);
        log.info("Found {}", product);

        return createDtoFromEntity(product);
    }

    @Override
    public List<ProductDto> findAllByCodes(List<String> codes) {

        List<ProductDto> products = productRepository.findAllByCodeIn(codes).stream()
                .map(this::createDtoFromEntity)
                .collect(Collectors.toList());

        log.info("Found {} of {} product(s)", products.size(), codes.size());
        return products;
    }

    @Override
    @Transactional
    public void deleteProduct(String code) {

        Product product = getProductByCodeOrElseThrow(code);
        productRepository.delete(product);
        log.info("Deleting product: {}", product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> search(String query) {

        List<ProductDto> products = productRepository.findAllByNameContaining(query).stream()
                .map(this::createDtoFromEntity)
                .collect(Collectors.toList());

        log.info("Found {} product(s) by query '{}'", products.size(), query);
        return products;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductDto> findAllByCategoryId(Long categoryId) {

        List<ProductDto> products = productRepository.findAllByCategoryId(categoryId).stream()
                .map(this::createDtoFromEntity)
                .collect(Collectors.toList());

        log.info("Found {} product(s) by category_id '{}'", products.size(), categoryId);
        return products;
    }

    private ProductDto createDtoFromEntity(Product product) {
        return ProductDto.builder()
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .amount(product.getAmount())
                .build();
    }
}
