package com.mikhailkarpov.products.controller;

import com.mikhailkarpov.products.dto.ProductDto;
import com.mikhailkarpov.products.entity.Product;
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

    @PostMapping("/products")
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto,
                                                    UriComponentsBuilder uriComponentsBuilder) {

        log.info("Request to create {}", productDto);
        Product product = productService.createProduct(productDto);

        URI location = uriComponentsBuilder.path("/products/{code}").build(product.getCode());

        return ResponseEntity
                .created(location)
                .body(mapDto(product));
    }

    @GetMapping("/products")
    public List<ProductDto> findAll(@RequestParam(value = "name", required = false, defaultValue = "") String name,
                                    @RequestParam(value = "code", required = false) List<String> codes) {

        log.info("Request for all products");
        List<Product> products = productService.findAll(name, codes);

        return products.stream()
                .map(this::mapDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/products/{code}")
    public ProductDto findProductByCode(@PathVariable String code) {

        log.info("Request for product '{}'", code);
        Product product = productService.findProductByCode(code);
        return mapDto(product);
    }

    @PutMapping("/products/{code}")
    public ProductDto updateProduct(@PathVariable String code, @Valid @RequestBody ProductDto update) {

        log.info("Request to update product '{}': {}", code, update);
        Product product = productService.updateProduct(code, update);

        return mapDto(product);
    }

    @GetMapping("/products/list")
    public List<ProductDto> findProductsByCodesIn(@RequestParam("code") List<String> codes) {

        log.info("Request for products list: {}", codes);
        return productService
                .findProductsByCodesIn(codes)
                .stream()
                .map(this::mapDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/products/search")
    public List<ProductDto> searchProducts(@RequestParam String query) {

        log.info("Request to search products '{}'", query);
        return productService
                .searchProducts(query)
                .stream()
                .map(this::mapDto)
                .collect(Collectors.toList());
    }

    private ProductDto mapDto(Product product) {

        return ProductDto.builder()
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .amount(product.getAmount())
                .build();
    }
}
