package com.mikhailkarpov.products.persistence.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Test
    void constructor() {
        //given
        Category category = new Category("name");

        //then
        assertEquals("name", category.getName());
        assertNull(category.getId());
        assertNull(category.getParent());
        assertTrue(category.getSubcategories().isEmpty());
        assertTrue(category.getProducts().isEmpty());
    }

    @Test
    void createSubcategory() {
        //given
        Category parent = new Category("parent");

        //then
        Category subcategory = parent.createSubcategory("subcategory");

        assertEquals("subcategory", subcategory.getName());
        assertEquals(parent, subcategory.getParent());
        assertTrue(parent.getSubcategories().contains(subcategory));
    }

    @Test
    void move() {
        //given
        Category parent = new Category("parent");
        Category subcategory = parent.createSubcategory("subcategory");
        Category newParent = new Category("new_parent");

        //then
        subcategory.move(newParent);

        assertFalse(parent.getSubcategories().contains(subcategory));
        assertTrue(newParent.getSubcategories().contains(subcategory));
        assertEquals(newParent, subcategory.getParent());
    }

    @Test
    void addProduct() {
        //given
        Category category = new Category("name");
        Product product = new Product("abc", "product", "description", 1000, 10);

        //then
        category.addProduct(product);

        assertTrue(category.getProducts().contains(product));
        assertTrue(product.getCategories().contains(category));
    }

    @Test
    void removeProduct() {
        //given
        Category category = new Category("name");
        Product product = new Product("abc", "product", "description", 1000, 10);
        category.addProduct(product);

        //then
        category.removeProduct(product);

        assertFalse(category.getProducts().contains(product));
        assertFalse(product.getCategories().contains(category));
    }
}