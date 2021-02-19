package com.mikhailkarpov.products.persistence.specification;

import com.mikhailkarpov.products.persistence.entity.Category;
import com.mikhailkarpov.products.persistence.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;

@AllArgsConstructor
@EqualsAndHashCode
public class ProductSpecification implements Specification<Product> {

    private String name;

    private List<String> codes;

    private Integer categoryId;

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        return nameLike(name)
                .and(categoryIdEquals(categoryId))
                .and(codeIn(codes))
                .toPredicate(root, query, criteriaBuilder);
    }

    private Specification<Product> nameLike(String name) {
        return (root, query, criteriaBuilder) ->
                name == null ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.like(criteriaBuilder.upper(root.get("name")), name.toUpperCase());
    }

    private Specification<Product> categoryIdEquals(Integer id) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, Category> join = root.join("categories", JoinType.LEFT);
            return id == null ?
                    criteriaBuilder.conjunction() :
                    criteriaBuilder.equal(join.get("id"), id);
        };
    }

    private Specification<Product> codeIn(List<String> codes) {
        return (root, query, criteriaBuilder) ->
                codes == null || codes.isEmpty() ?
                        criteriaBuilder.conjunction() :
                        root.get("code").in(codes);
    }

}

