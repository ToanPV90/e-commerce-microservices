package com.mikhailkarpov.products.service;

import com.mikhailkarpov.products.dto.ProductDto;
import com.mikhailkarpov.products.exception.BadRequestException;
import com.mikhailkarpov.products.exception.ResourceAlreadyExistsException;
import com.mikhailkarpov.products.exception.ResourceNotFoundException;
import com.mikhailkarpov.products.persistence.entity.Product;
import com.mikhailkarpov.products.persistence.repository.ProductRepository;
import com.mikhailkarpov.products.persistence.specification.ProductSpecification;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    private final ProductRepository productRepository = mock(ProductRepository.class);

    private final ProductServiceImpl productService = new ProductServiceImpl(productRepository);

    @Test
    void createProduct() {
        //given
        ProductDto productDto = new ProductDto("ABC", "abc product", "description", 1100, 11);

        //when
        when(productRepository.existsByCode("ABC")).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(mock(Product.class));

        //then
        Product product = productService.createProduct(productDto);

        verify(productRepository).existsByCode("ABC");
        verify(productRepository).save(any(Product.class));
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void givenExistsByCode_whenCreateProduct_thenThrows() {
        //given
        ProductDto productDto = new ProductDto("ABC", "abc product", "description", 1100, 11);

        //when
        when(productRepository.existsByCode("ABC")).thenReturn(true);

        //then
        assertThrows(ResourceAlreadyExistsException.class, () -> productService.createProduct(productDto));

        verify(productRepository).existsByCode("ABC");
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void findProductByCode() {
        //when
        when(productRepository.findByCode("ABC")).thenReturn(Optional.of(mock(Product.class)));

        //then
        assertNotNull(productService.findProductByCode("ABC"));

        verify(productRepository).findByCode("ABC");
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void givenNotExistsByCode_whenFindByproductCode_thenThrows() {
        assertThrows(ResourceNotFoundException.class, () -> productService.findProductByCode("ABC"));
    }

    @Test
    void findAll() {
        //when
        when(productRepository.findAll()).thenReturn(mock(List.class));

        //then
        List<Product> products = productService.findAll();

        assertNotNull(products);
        assertFalse(products.isEmpty());

        verify(productRepository).findAll();
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void findBySpecification() {
        //when
        when(productRepository.findAll(any(Specification.class))).thenReturn(mock(List.class));

        //then
        List<Product> bySpecification = productService.findBySpecification(new ProductSpecification(null, null, null));
        assertNotNull(bySpecification);

        verify(productRepository).findAll(any(Specification.class));
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void updateProduct() {
        //given
        Product product = new Product("ABC", "old_name", "old_description", 1000, 10);
        ProductDto update = new ProductDto("ABC", "new_name", "new_description", 2000, 20);

        //when
        when(productRepository.findByCode("ABC")).thenReturn(Optional.of(product));

        //then
        Product updated = productService.updateProduct("ABC", update);
        assertEquals("ABC", updated.getCode());
        assertEquals("new_name", updated.getName());
        assertEquals("new_description", updated.getDescription());
        assertEquals(2000, updated.getPrice());
        assertEquals(20, updated.getAmount());

        verify(productRepository).findByCode("ABC");
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void givenDifferentCode_whenUpdateProduct_thenThrows() {
        //given
        Product product = new Product("ABC", "old_name", "old_description", 1000, 10);
        ProductDto update = new ProductDto("XYZ", "new_name", "new_description", 2000, 20);

        //when
        when(productRepository.findByCode("ABC")).thenReturn(Optional.of(product));

        //then
        assertThrows(BadRequestException.class, () -> productService.updateProduct("ABC", update));
    }

    @Test
    void givenNoProduct_whenUpdateProduct_thenThrows() {
        //given
        ProductDto update = new ProductDto("ABC", "new_name", "new_description", 2000, 20);

        //when
        when(productRepository.findByCode("ABC")).thenReturn(Optional.empty());

        //then
        assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct("ABC", update));
    }
}