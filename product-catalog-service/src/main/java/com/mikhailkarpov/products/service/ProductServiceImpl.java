package com.mikhailkarpov.products.service;

import com.mikhailkarpov.products.dto.ProductDto;
import com.mikhailkarpov.products.entity.Product;
import com.mikhailkarpov.products.exception.BadRequestException;
import com.mikhailkarpov.products.exception.ResourceAlreadyExistsException;
import com.mikhailkarpov.products.exception.ResourceNotFoundException;
import com.mikhailkarpov.products.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Product createProduct(ProductDto product) {

        if (productRepository.existsByCode(product.getCode())) {
            String message = String.format("Product '%s' already exists", product.getCode());
            throw new ResourceAlreadyExistsException(message);
        }

        Product created = productRepository.save(Product.builder()
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .amount(product.getAmount())
                .build());

        log.info("Saving {}", created);
        return created;
    }

    @Override
    @Transactional(readOnly = true)
    public Product findProductByCode(String code) {

        return productRepository.findByCode(code).orElseThrow(() -> {
            String message = String.format("Product with code '%s' not found", code);
            return new ResourceNotFoundException(message);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {

        List<Product> products = new ArrayList<>();
        productRepository.findAll().forEach(products::add);
        products.sort(Comparator.comparing(Product::getCode));

        return products;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findProductsByCategoryId(Integer categoryId) {

        return productRepository.findAllByCategoriesId(categoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findProductsByCodesIn(List<String> codes) {

        return productRepository.findAllByCodeIn(codes);
    }

    @Override
    public List<Product> searchProducts(String query) {

        return productRepository.findAllByNameContainingIgnoreCase(query);
    }

    @Override
    public Product updateProduct(String code, ProductDto update) {

        if (code.equals(update.getCode())) {
            String message = String.format("Expected code '%s', but was '%s'", code, update.getCode());
            throw new BadRequestException(message);
        }

        Product product = findProductByCode(code);
        product.setName(update.getName());
        product.setDescription(update.getDescription());
        product.setPrice(update.getPrice());
        product.setAmount(update.getAmount());

        log.info("Updating {}", product);
        return product;
    }

    @Override
    @Transactional
    public List<Product> findAll(String name, List<String> codes) {

        return productRepository.search(name, codes);
    }
}
