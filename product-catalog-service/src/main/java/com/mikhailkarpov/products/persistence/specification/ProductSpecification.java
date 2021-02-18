package com.mikhailkarpov.products.persistence.specification;

import com.mikhailkarpov.products.persistence.entity.Category;
import com.mikhailkarpov.products.persistence.entity.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import java.util.List;

@Component
public class ProductSpecification {

    public Specification<Product> nameLike(String name) {
        return (root, query, criteriaBuilder) ->
            name == null ?
                    criteriaBuilder.conjunction() :
                    criteriaBuilder.like(criteriaBuilder.upper(root.get("name")), name.toUpperCase());
    }

    public Specification<Product> categoryIdEquals(Integer id) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, Category> join = root.join("categories", JoinType.LEFT);
            return id == null ?
                    criteriaBuilder.conjunction() :
                    criteriaBuilder.equal(join.get("id"), id);
        };
    }

    public Specification<Product> codeIn(List<String> codes) {
        return (root, query, criteriaBuilder) ->
                codes == null || codes.isEmpty() ?
                        criteriaBuilder.conjunction() :
                        root.get("code").in(codes);
    }
}

