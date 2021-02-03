package com.mikhailkarpov.orders.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity(name = "Order")
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "account_id", nullable = false, updatable = false)
    private String accountId;

    @OneToMany(fetch = LAZY, mappedBy = "order", cascade = PERSIST, orphanRemoval = true)
    private Set<OrderItem> items = new HashSet<>();

    @Column(name = "status")
    @Enumerated(value = STRING)
    private OrderStatus status;

    protected Order() {
        // for JPA
    }

    public Order(String accountId, OrderStatus status) {
        this.accountId = accountId;
        this.status = status;
    }

    public Order(String accountId, OrderStatus status, Set<OrderItem> items) {
        this(accountId, status);
        setItems(items);
    }

    public UUID getId() {
        return id;
    }

    public String getAccountId() {
        return accountId;
    }

    public Set<OrderItem> getItems() {
        return items;
    }

    public void setItems(Set<OrderItem> items) {
        this.items = items;
        for (OrderItem item : this.items) {
            item.setOrder(this);
        }
    }

    public void addItem(OrderItem item) {
        if (items.add(item)) {
            item.setOrder(this);
        }
    }

    public void removeItem(OrderItem item) {
        if (items.remove(item)) {
            item.setOrder(null);
        }
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", accountId='" + accountId + '\'' +
                ", status=" + status +
                '}';
    }
}
