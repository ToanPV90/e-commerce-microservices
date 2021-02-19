package com.mikhailkarpov.products.service;

import com.mikhailkarpov.products.dto.ProductDto;
import com.mikhailkarpov.products.exception.BadRequestException;
import com.mikhailkarpov.products.exception.ResourceAlreadyExistsException;
import com.mikhailkarpov.products.exception.ResourceNotFoundException;
import com.mikhailkarpov.products.persistence.entity.Product;
import com.mikhailkarpov.products.persistence.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        Product created = productRepository.save(new Product(
                product.getCode(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getAmount()));

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

        return productRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findBySpecification(Specification<Product> specification) {

        return productRepository.findAll(specification);
    }

    @Override
    @Transactional
    public Product updateProduct(String code, ProductDto update) {

        if (!code.equals(update.getCode())) {
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

}
