package com.mikhailkarpov.products.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity(name = "Category")
@Table(name = "categories")
@Getter
@Setter
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
    private Set<Category> subcategories = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "categories")
    private Set<Product> products = new HashSet<>();

    public Category() {
        //for JPA
    }

    public Category(String name) {
        this.name = name;
    }

    public Category createSubcategory(String name) {

        Category subcategory = new Category(name);
        subcategory.parent = this;
        this.subcategories.add(subcategory);

        return subcategory;
    }

    public boolean move(Category destination) {

        if (this.parent != null && this.parent.subcategories.remove(this)) {
            this.parent = destination;
            return destination.subcategories.add(this);
        }
        return false;
    }

    public List<Category> getSubcategories() {
        return new ArrayList<>(subcategories);
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Category category = (Category) o;

        return this.name.equals(category.name) && Objects.equals(id, category.getId());
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
