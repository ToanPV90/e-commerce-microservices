package com.mikhailkarpov.orders.entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "OrderItem")
@Table(name = "orders_items")
public class OrderItem {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_fk", nullable = false, updatable = false)
    private Order order;

    @Column(name = "code", nullable = false, updatable = false)
    private String code;

    @Column(name = "name", nullable = false, updatable = false)
    private String name;

    @Column(name = "price", nullable = false, updatable = false)
    private Integer price;

    @Column(name = "amount", nullable = false, updatable = false)
    private Integer amount;

    protected OrderItem() {
        // for JPA
    }

    public OrderItem(String code, String name, Integer price, Integer amount) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    public UUID getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    protected void setOrder(Order order) {
        this.order = order;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderItem item = (OrderItem) o;

        return code.equals(item.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", amount=" + amount +
                '}';
    }
}
