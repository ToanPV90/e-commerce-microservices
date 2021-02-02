package com.mikhailkarpov.products.service;

import com.mikhailkarpov.products.dto.ProductDto;
import com.mikhailkarpov.products.entity.Product;
import com.mikhailkarpov.products.exception.ResourceAlreadyExistsException;
import com.mikhailkarpov.products.exception.ResourceNotFoundException;
import com.mikhailkarpov.products.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public ProductDto createProduct(ProductDto product) {
        String code = product.getCode();

        if (productRepository.existsByCode(code)) {
            String message = String.format("Product with code={} already exists");
            throw new ResourceAlreadyExistsException(message);
        }

        String name = product.getName();
        String description = product.getDescription();
        Integer amount = product.getAmount();

        Product created = productRepository.save(new Product(code, name, description, amount));
        log.info("Creating product: {}", created);

        product.setId(created.getId());
        return product;
    }

    @Override
    @Transactional
    public ProductDto updateProduct(String code, ProductDto update) {
        Product product = getProductByCodeOrElseThrow(code);

        product.setName(update.getName());
        product.setDescription(update.getDescription());
        product.setAmount(update.getAmount());
        log.info("Updating product: {}", product);

        return update;
    }

    private Product getProductByCodeOrElseThrow(String code) {
        Optional<Product> found = productRepository.findByCode(code);

        if (!found.isPresent()) {
            String message = String.format("Product with code={} not found", code);
            throw new ResourceNotFoundException(message);
        }

        Product product = found.get();
        log.info("Found product: {}", product);
        return product;
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDto findProductByCode(String code) {
        Product product = getProductByCodeOrElseThrow(code);
        return createDtoFromEntity(product);
    }

    @Override
    public List<ProductDto> findAllProducts() {
        List<ProductDto> products = new ArrayList<>();
        productRepository.findAll().forEach(product -> products.add(createDtoFromEntity(product)));
        log.info("Found {} product(s)", products.size());
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
        return productRepository.findAllByNameContaining(query)
                .stream()
                .map(this::createDtoFromEntity)
                .collect(Collectors.toList());
    }

    private ProductDto createDtoFromEntity(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .amount(product.getAmount())
                .build();
    }
}
