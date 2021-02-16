package com.mikhailkarpov.products.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.*;

@Entity(name = "Product")
@Table(name = "products")
public class Product {

    @Id
    @Column(name = "code", nullable = false, unique = true)
    @Getter
    @Setter
    private String code;

    @Column(name = "name", nullable = false)
    @Getter
    @Setter
    private String name;

    @Column(name = "description", nullable = false)
    @Getter
    @Setter
    private String description;

    @Column(name = "price", nullable = false)
    @Getter
    @Setter
    private Integer price;

    @Column(name = "amount", nullable = false)
    @Getter
    @Setter
    private Integer amount;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "categories_products",
            joinColumns = @JoinColumn(name = "product_fk"),
            inverseJoinColumns = @JoinColumn(name = "category_fk")
    )
    private Set<Category> categories = new HashSet<>();

    public Product() {
        // for JPA
    }

    protected void addTo(Category category) {
        this.categories.add(category);
    }

    protected void removeFrom(Category category) {
        this.categories.remove(category);
    }

    public List<Category> getCategories() {
        return Collections.unmodifiableList(new ArrayList<>(categories));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return code.equals(product.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "Product{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price='" + price + '\'' +
                ", amount=" + amount +
                '}';
    }
}
