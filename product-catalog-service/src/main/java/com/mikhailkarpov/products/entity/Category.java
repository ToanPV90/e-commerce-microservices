package com.mikhailkarpov.products.entity;

import javax.persistence.*;
import java.util.*;

@Entity(name = "Category")
@Table(name = "categories")
public class Category {

    @Id
    @SequenceGenerator(name = "categories_id_seq", sequenceName = "categories_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categories_id_seq")
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private Category parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private final Set<Category> subcategories = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "categories")
    private final Set<Product> products = new HashSet<>();

    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getParent() {
        return parent;
    }

    public List<Category> getSubcategories() {
        return Collections.unmodifiableList(new ArrayList<>(subcategories));
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(new ArrayList<>(products));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        return this.name.equals(category.name);
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + getName() + '\'' +
                '}';
    }
}
