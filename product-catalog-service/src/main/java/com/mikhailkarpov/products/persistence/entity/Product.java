package com.mikhailkarpov.products.persistence.entity;

import javax.persistence.*;
import java.util.*;

@Entity(name = "Product")
@Table(name = "products")
public class Product {

    @Id
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "products")
    private Set<Category> categories = new HashSet<>();

    protected Product() {
        // for JPA
    }

    public Product(String code, String name, String description, Integer price, Integer amount) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public List<Category> getCategories() {
        return Collections.unmodifiableList(new ArrayList<>(categories));
    }

    protected void addToCategory(Category category) {
        this.categories.add(category);
    }

    protected void removeFromCategory(Category category) {
        this.categories.remove(category);
    }

    protected void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

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
