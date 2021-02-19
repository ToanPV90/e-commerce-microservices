package com.mikhailkarpov.products.service;

import com.mikhailkarpov.products.exception.ResourceNotFoundException;
import com.mikhailkarpov.products.persistence.entity.Category;
import com.mikhailkarpov.products.persistence.entity.Product;
import com.mikhailkarpov.products.persistence.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceImplTest {

    private final CategoryRepository categoryRepository = mock(CategoryRepository.class);

    private final ProductService productService = mock(ProductService.class);

    private final CategoryService categoryService = new CategoryServiceImpl(categoryRepository, productService);

    private final Category mockCategory = mock(Category.class);

    @Test
    void createCategory() {
        //given
        Category category = new Category("name");

        //when
        when(categoryRepository.save(category)).thenReturn(new Category("name"));

        //then
        Category created = categoryService.createCategory("name");

        assertEquals("name", created.getName());
        verify(categoryRepository).save(category);
    }

    @Test
    void createSubcategory() {
        //given
        Category parent = new Category("parent");
        Category subcategory = new Category("subcategory");

        //when
        when(categoryRepository.findById(1)).thenReturn(Optional.of(parent));
        when(categoryRepository.save(subcategory)).thenReturn(subcategory);

        //then
        Category created = categoryService.createSubcategory(1, "subcategory");

        assertEquals("subcategory", created.getName());
        assertEquals(parent, created.getParent());
        assertTrue(parent.getSubcategories().contains(created));

        verify(categoryRepository).save(created);
    }

    @Test
    void findById() {
        //given
        Category category = new Category("category");

        //when
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        //then
        Category byId = categoryService.findById(1);
        assertEquals("category", byId.getName());
    }

    @Test
    void finById_whenNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> categoryService.findById(1));
    }

    @Test
    void findAll() {
        //given
        List<Category> categories = Arrays.asList(mockCategory, mockCategory);

        //when
        when(categoryRepository.findAll()).thenReturn(categories);

        //then
        assertEquals(2, categoryService.findAll(true).size());
        verify(categoryRepository).findAll();
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void findAllParent() {
        //when
        when(categoryRepository.findAllByParentId(null)).thenReturn(Arrays.asList(mockCategory, mockCategory));

        //then
        assertEquals(2, categoryService.findAll(false).size());
        verify(categoryRepository).findAllByParentId(null);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    void findSubcategoriesByParentId() {
        //given
        List<Category> categories = Arrays.asList(mockCategory, mockCategory);

        //when
        when(categoryRepository.findAllByParentId(1)).thenReturn(categories);

        //then
        List<Category> subcategories = categoryService.findSubcategoriesByParentId(1);
        assertEquals(categories, subcategories);
    }

    @Test
    void moveCategory() {
        //given
        Category parent = Mockito.mock(Category.class);

        //when
        when(categoryRepository.findById(1)).thenReturn(Optional.of(mockCategory));
        when(categoryRepository.findById(2)).thenReturn(Optional.of(parent));

        //then
        categoryService.moveCategory(1, 2);
        verify(mockCategory).move(parent);
    }

    @Test
    void renameCategory() {
        //when
        when(categoryRepository.findById(1)).thenReturn(Optional.of(new Category("name")));

        //then
        Category rename = categoryService.renameCategory(1, "rename");
        assertEquals("rename", rename.getName());
    }

    @Test
    void addProduct() {
        //given
        Category category = new Category("name");
        Product product = new Product("ABC", "product", "description", 11, 12);

        //when
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(productService.findProductByCode("ABC")).thenReturn(product);

        //then
        categoryService.addProduct(1, "ABC");
        assertTrue(category.getProducts().contains(product));
        assertTrue(product.getCategories().contains(category));
    }

    @Test
    void removeProduct() {
        //given
        Category category = new Category("name");
        Product product = new Product("ABC", "product", "description", 11, 12);
        category.addProduct(product);

        //when
        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));
        when(productService.findProductByCode("ABC")).thenReturn(product);

        //then
        assertTrue(category.getProducts().contains(product));
        assertTrue(product.getCategories().contains(category));

        categoryService.removeProduct(1, "ABC");

        assertFalse(category.getProducts().contains(product));
        assertFalse(product.getCategories().contains(category));
    }
}