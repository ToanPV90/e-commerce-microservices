package com.mikhailkarpov.products.controller;

import com.mikhailkarpov.products.dto.ProductDto;
import com.mikhailkarpov.products.controller.mapper.ProductMapper;
import com.mikhailkarpov.products.persistence.entity.Product;
import com.mikhailkarpov.products.persistence.specification.ProductSpecification;
import com.mikhailkarpov.products.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final ProductMapper productMapper;

    @PostMapping("/products")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto,
                                                    UriComponentsBuilder uriComponentsBuilder) {

        log.info("Request to create {}", productDto);
        Product product = productService.createProduct(productDto);

        URI location = uriComponentsBuilder.path("/products/{code}").build(product.getCode());

        return ResponseEntity
                .created(location)
                .body(productMapper.map(product));
    }

    @GetMapping("/products")
    public List<ProductDto> findAll(@RequestParam(value = "name", required = false) String name,
                                    @RequestParam(value = "code", required = false) List<String> codes,
                                    @RequestParam(value = "category", required = false) Integer categoryId) {
        //todo sorting and paging

        log.info("Request for products: name={}, codes={}, category_id={}", name, codes, categoryId);
        ProductSpecification specification = new ProductSpecification(name, codes, categoryId);

        return productService.findBySpecification(specification)
                .stream()
                .map(productMapper::map)
                .collect(Collectors.toList());
    }

    @GetMapping("/products/{code}")
    public ProductDto findProductByCode(@PathVariable String code) {

        log.info("Request for product '{}'", code);
        Product product = productService.findProductByCode(code);

        return productMapper.map(product);
    }

    @PutMapping("/products/{code}")
    public ProductDto updateProduct(@PathVariable String code, @Valid @RequestBody ProductDto update) {

        log.info("Request to update product '{}': {}", code, update);
        Product product = productService.updateProduct(code, update);

        return productMapper.map(product);
    }
}
