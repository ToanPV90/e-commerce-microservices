package com.mikhailkarpov.products.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mikhailkarpov.products.controller.mapper.ProductMapper;
import com.mikhailkarpov.products.dto.ProductDto;
import com.mikhailkarpov.products.persistence.entity.Product;
import com.mikhailkarpov.products.persistence.specification.ProductSpecification;
import com.mikhailkarpov.products.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;

@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProductControllerTest {

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductMapper productMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
                .andExpect(header().string("Location", "http://localhost/products/ABC"))
                .andExpect(jsonPath("$.code").value("ABC"))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.price").value(10))
                .andExpect(jsonPath("$.amount").value(12));

        verify(productService).createProduct(dto);
        verify(productMapper).map(product);
        verifyNoMoreInteractions(productService, productMapper);
    }

    @ParameterizedTest
    @MethodSource("getNotValidProductDto")
    void givenNotValidRequest_whenCreateProduct_then400(ProductDto dto) throws Exception {

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(productService, productMapper);
    }

    static Stream<Arguments> getNotValidProductDto() {
        return Stream.of(
                Arguments.of(new ProductDto(null, "name", "description", 100, 10)),
                Arguments.of(new ProductDto("code", null, "description", 100, 10)),
                Arguments.of(new ProductDto("code", "name", null, 100, 10)),
                Arguments.of(new ProductDto("code", "name", "description", null, 10)),
                Arguments.of(new ProductDto("code", "name", "description", 100, null)),
                Arguments.of(new ProductDto("code", "name", "description", -1, 10)),
                Arguments.of(new ProductDto("code", "name", "description", 100, -1))
        );
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

        ProductSpecification specification = new ProductSpecification(null, null, null);

        when(productService.findBySpecification(specification)).thenReturn(products);
        when(productMapper.map(products.get(0))).thenReturn(productsDto.get(0));
        when(productMapper.map(products.get(1))).thenReturn(productsDto.get(1));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(productService).findBySpecification(specification);
        verify(productMapper).map(products.get(0));
        verify(productMapper).map(products.get(1));
        verifyNoMoreInteractions(productService, productMapper);
    }

    @Test
    void findAll_withName() throws Exception {
        List<Product> products = Arrays.asList(
                new Product("ABC", "name 1", "description 2", 10, 12),
                new Product("XYZ", "name 2", "description 1", 20, 22)
        );
        List<ProductDto> productsDto = Arrays.asList(
                new ProductDto("ABC", "name 1", "description 2", 10, 12),
                new ProductDto("XYZ", "name 2", "description 1", 20, 22)
        );

        List<String> codes = Arrays.asList("ABC", "XYZ");
        ProductSpecification specification = new ProductSpecification("name", null, null);

        when(productService.findBySpecification(specification)).thenReturn(products);
        when(productMapper.map(products.get(0))).thenReturn(productsDto.get(0));
        when(productMapper.map(products.get(1))).thenReturn(productsDto.get(1));

        mockMvc.perform(get("/products?name={name}", "name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(productService).findBySpecification(specification);
        verify(productMapper).map(products.get(0));
        verify(productMapper).map(products.get(1));
        verifyNoMoreInteractions(productService, productMapper);
    }

    @Test
    void findAll_withCodeIn() throws Exception {
        List<Product> products = Arrays.asList(
                new Product("ABC", "name 1", "description 2", 10, 12),
                new Product("XYZ", "name 2", "description 1", 20, 22)
        );
        List<ProductDto> productsDto = Arrays.asList(
                new ProductDto("ABC", "name 1", "description 2", 10, 12),
                new ProductDto("XYZ", "name 2", "description 1", 20, 22)
        );

        List<String> codes = Arrays.asList("ABC", "XYZ");
        ProductSpecification specification = new ProductSpecification(null, codes, null);

        when(productService.findBySpecification(specification)).thenReturn(products);
        when(productMapper.map(products.get(0))).thenReturn(productsDto.get(0));
        when(productMapper.map(products.get(1))).thenReturn(productsDto.get(1));

        mockMvc.perform(get("/products?code={code1}&code={code2}", "ABC", "XYZ"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(productService).findBySpecification(specification);
        verify(productMapper).map(products.get(0));
        verify(productMapper).map(products.get(1));
        verifyNoMoreInteractions(productService, productMapper);
    }

    @Test
    void findAll_withCategoryId() throws Exception {
        List<Product> products = Arrays.asList(
                new Product("ABC", "name 1", "description 2", 10, 12),
                new Product("XYZ", "name 2", "description 1", 20, 22)
        );
        List<ProductDto> productsDto = Arrays.asList(
                new ProductDto("ABC", "name 1", "description 2", 10, 12),
                new ProductDto("XYZ", "name 2", "description 1", 20, 22)
        );

        ProductSpecification specification = new ProductSpecification(null, null, 2);

        when(productService.findBySpecification(specification)).thenReturn(products);
        when(productMapper.map(products.get(0))).thenReturn(productsDto.get(0));
        when(productMapper.map(products.get(1))).thenReturn(productsDto.get(1));

        mockMvc.perform(get("/products?category={id}", 2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));

        verify(productService).findBySpecification(specification);
        verify(productMapper).map(products.get(0));
        verify(productMapper).map(products.get(1));
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
                        "\"name\": \"updated name\"," +
                        "\"description\": \"updated description\"," +
                        "\"price\": 20," +
                        "\"amount\": 22" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("ABC"))
                .andExpect(jsonPath("$.name").value("updated name"))
                .andExpect(jsonPath("$.description").value("updated description"))
                .andExpect(jsonPath("$.price").value(20))
                .andExpect(jsonPath("$.amount").value(22));

        verify(productService).updateProduct(any(String.class), any(ProductDto.class));
        verify(productMapper).map(any(Product.class));
        verifyNoMoreInteractions(productService, productMapper);
    }

    @ParameterizedTest
    @MethodSource("getNotValidProductDto")
    void givenNotValidProductDto_whenUpdateProduct_then400(ProductDto dto) throws Exception {

        mockMvc.perform(put("/products/{code}", "code")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(productService, productMapper);
    }
}