package com.mikhailkarpov.customers.entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "Address")
@Table(name = "addresses")
public class Address {

    @Id
    @Column(name = "customer_fk")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    private Customer customer;

    @Column(name = "zip", nullable = false)
    private String zip;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "street", nullable = false)
    private String street;

    protected Address() {
        // for JPA
    }

    public Address(String zip, String country, String city, String street) {
        this.zip = zip;
        this.country = country;
        this.city = city;
        this.street = street;
    }

    public UUID getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(id, address.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, zip, country, city, street);
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", zip='" + zip + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                '}';
    }
}
