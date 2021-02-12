package com.mikhailkarpov.products.entity;

import lombok.Builder;

import javax.persistence.*;

@Entity(name = "Product")
@Table(name = "products")
@Builder
public class Product {

    @Id
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_fk")
    private Category category;

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

    public Product(String code, String name, String description, Integer price, Integer amount, Category category) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
        this.category = category;
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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
                ", amount=" + amount +
                '}';
    }
}
