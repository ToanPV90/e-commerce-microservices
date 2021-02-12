package com.mikhailkarpov.products.entity;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.*;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;

@Entity(name = "Category")
@Table(name = "categories")
public class Category {

    private static final Logger log = LoggerFactory.getLogger(Category.class);

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(fetch = LAZY, mappedBy = "category", cascade = {PERSIST, MERGE})
    private Set<Product> products = new HashSet<>();

    protected Category() {
        // for JPA
    }

    public Category(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Product> getProducts() {
        return Collections.unmodifiableCollection(products);
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public void addProduct(Product product) {
        if (products.add(product)) {
            product.setCategory(this);
            log.debug("Added {}", product);
        }
    }

    public void removeProduct(Product product) {
        if (products.remove(product)) {
            product.setCategory(null);
            log.debug("Removed {}", product);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        return getName().equals(category.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
