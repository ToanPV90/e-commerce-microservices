package com.mikhailkarpov.products.service;

import com.mikhailkarpov.products.entity.Category;
import com.mikhailkarpov.products.entity.Product;
import com.mikhailkarpov.products.exception.ResourceNotFoundException;
import com.mikhailkarpov.products.repository.ProductRepository;
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

    private final ProductRepository productRepository;
    private final EntityManager entityManager;

    @Override
    public Product findProductByCode(String code) {

        return productRepository.findByCode(code).orElseThrow(() -> {
            String message = String.format("Product with code '%s' not found", code);
            return new ResourceNotFoundException(message);
        });
    }

    @Override
    public List<Product> findProductsByCategoryId(Integer categoryId) {

        List<Category> categories = entityManager.createQuery(
                "SELECT c FROM Category c " +
                        "LEFT JOIN FETCH c.products " +
                        "WHERE c.id = :id", Category.class)
                .setParameter("id", categoryId)
                .getResultList();

        if (categories.isEmpty()) {
            String message = String.format("Category with id '%d' not found", categoryId);
            throw new ResourceNotFoundException(message);
        }

        return categories.get(0).getProducts();
    }

    @Override
    public List<Product> findProductsByCodesIn(List<String> codes) {

        return productRepository.findAllByCodeIn(codes);
    }

    @Override
    public List<Product> searchProducts(String query) {

        return productRepository.findAllByNameLike(query);
    }


}
