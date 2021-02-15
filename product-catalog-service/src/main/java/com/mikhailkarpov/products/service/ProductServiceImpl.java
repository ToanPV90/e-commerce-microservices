package com.mikhailkarpov.products.service;

import com.mikhailkarpov.products.dto.ProductDto;
import com.mikhailkarpov.products.entity.Product;
import com.mikhailkarpov.products.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {

    private final EntityManager entityManager;

    @Override
    public ProductDto findProductByCode(String code) {
        Product product = entityManager.find(Product.class, code);

        if (product == null) {
            String message = String.format("Product '%s' not found", code);
            throw new ResourceNotFoundException(message);
        }

        return ProductDto.builder()
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .amount(product.getAmount())
                .build();
    }

    @Override
    public List<ProductDto> findProductsByCategoryId(Integer categoryId) {

        return entityManager.createQuery(
                "SELECT new com.mikhailkarpov.products.dto.ProductDto" +
                        "(p.code, p.name, p.description, p.price, p.amount) " +
                        "FROM Product p " +
                        "WHERE p.category.id = :id", ProductDto.class)
                .setParameter("id", categoryId)
                .getResultList();
    }

    @Override
    public List<ProductDto> findProductsByCodesIn(List<String> codes) {
        return entityManager.createQuery(
                "SELECT new com.mikhailkarpov.products.dto.ProductDto" +
                        "(p.code, p.name, p.description, p.price, p.amount) " +
                        "FROM Product p " +
                        "WHERE p.code IN :codes", ProductDto.class)
                .setParameter("codes", codes)
                .getResultList();
    }

    @Override
    public List<ProductDto> searchProducts(String query) {
        return entityManager.createQuery(
                "SELECT new com.mikhailkarpov.products.dto.ProductDto" +
                        "(p.code, p.name, p.description, p.price, p.amount) " +
                        "FROM Product p " +
                        "WHERE lower(p.name) LIKE lower(CONCAT('%',:name,'%'))", ProductDto.class)
                .setParameter("name", query)
                .getResultList();
    }
}
