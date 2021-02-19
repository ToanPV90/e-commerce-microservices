package com.mikhailkarpov.products.controller;

import com.mikhailkarpov.products.controller.mapper.ProductMapper;
import com.mikhailkarpov.products.dto.ProductDto;
import com.mikhailkarpov.products.persistence.entity.Product;
import com.mikhailkarpov.products.persistence.specification.ProductSpecification;
import com.mikhailkarpov.products.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.context.named.NamedContextFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductMapper productMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createProduct() throws Exception {
        Product product = new Product("ABC", "name", "description", 10, 12);
        ProductDto dto = new ProductDto("ABC", "name", "description", 10, 12);

        when(productService.createProduct(dto)).thenReturn(product);
        when(productMapper.map(product)).thenReturn(dto);

        mockMvc.perform(post("/products")
                .contentType("application/json")
                .content("{" +
                        "\"code\": \"ABC\"," +
                        "\"name\": \"name\"," +
                        "\"description\": \"description\"," +
                        "\"price\": 10," +
                        "\"amount\": 12" +
                        "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value("ABC"))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.price").value(10))
                .andExpect(jsonPath("$.amount").value(12));

        verify(productService).createProduct(dto);
        verify(productMapper).map(product);
        verifyNoMoreInteractions(productService, productMapper);
    }

    @Test
    void findAll() throws Exception {
        List<Product> products = Arrays.asList(
                new Product("ABC", "name 1", "description 2", 10, 12),
                new Product("XYZ", "name 2", "description 1", 20, 22)
        );
        List<ProductDto> productsDto = Arrays.asList(
                new ProductDto("ABC", "name 1", "description 2", 10, 12),
                new ProductDto("XYZ", "name 2", "description 1", 20, 22)
        );

        when(productService.findBySpecification(new ProductSpecification(null, null, null))).thenReturn(products);
        when(productMapper.map(products.get(0))).thenReturn(productsDto.get(0));
        when(productMapper.map(products.get(1))).thenReturn(productsDto.get(1));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(productService).findBySpecification(any(ProductSpecification.class));
        verify(productMapper, times(2)).map(any(Product.class));
        verifyNoMoreInteractions(productService, productMapper);
    }

    @Test
    void findProductByCode() throws Exception {
        Product product = new Product("ABC", "name", "description", 10, 12);
        ProductDto dto = new ProductDto("ABC", "name", "description", 10, 12);

        when(productService.findProductByCode("ABC")).thenReturn(product);
        when(productMapper.map(any())).thenReturn(dto);

        mockMvc.perform(get("/products/ABC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("ABC"))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.price").value(10))
                .andExpect(jsonPath("$.amount").value(12));

        verify(productService).findProductByCode("ABC");
        verify(productMapper).map(product);
        verifyNoMoreInteractions(productService, productMapper);
    }

    @Test
    void updateProduct() throws Exception {
        Product product = new Product("ABC", "name", "description", 10, 12);
        ProductDto update = new ProductDto("ABC", "updated name", "updated description", 20, 22);

        when(productService.updateProduct("ABC", update)).thenReturn(product);
        when(productMapper.map(any())).thenReturn(update);

        mockMvc.perform(put("/products/ABC")
                .contentType("application/json")
                .content("{" +
                        "\"code\": \"ABC\"," +
                        "\"name\": \"name\"," +
                        "\"description\": \"description\"," +
                        "\"price\": 10," +
                        "\"amount\": 12" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("ABC"))
                .andExpect(jsonPath("$.name").value("updated name"))
                .andExpect(jsonPath("$.description").value("updated description"))
                .andExpect(jsonPath("$.price").value(20))
                .andExpect(jsonPath("$.amount").value(22));

        verify(productService).updateProduct("ABC", update);
        verify(productMapper).map(product);
        verifyNoMoreInteractions(productService, productMapper);
    }
}