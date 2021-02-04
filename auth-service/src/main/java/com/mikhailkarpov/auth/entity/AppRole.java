package com.mikhailkarpov.auth.entity;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "AppRole")
@Table(name = "roles")
public class AppRole {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    protected AppRole() {
        // for JPA
    }

    public AppRole(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppRole appRole = (AppRole) o;

        return name.equals(appRole.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "AppRole{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
