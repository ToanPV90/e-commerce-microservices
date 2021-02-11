package com.mikhailkarpov.customers.entity;

import javax.persistence.*;
import java.util.UUID;

import static javax.persistence.CascadeType.*;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.FetchType.LAZY;

@Entity(name = "Customer")
@Table(name = "customers")
public class Customer {

    @Id
    private UUID id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @OneToOne(fetch = LAZY, orphanRemoval = true, mappedBy = "customer", cascade = {PERSIST, MERGE})
    private Address address;

    protected Customer() {
        // for JPA
    }

    public Customer(UUID id, String email) {
        this.id = id;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        if (this.address != null) {
            this.address.setCustomer(null);
        }

        this.address = address;
        if (address != null) {
            address.setCustomer(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        return id.equals(customer.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", email='" + email + '\'' +
                '}';
    }
}
