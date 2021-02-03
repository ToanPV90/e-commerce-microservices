package com.mikhailkarpov.products.controller;

import com.mikhailkarpov.products.dto.ProductDto;
import com.mikhailkarpov.products.exception.BadRequestException;
import com.mikhailkarpov.products.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto product,
                                                    UriComponentsBuilder uriComponentsBuilder) {

        log.info("Request for creating product: {}", product);
        ProductDto created = productService.createProduct(product);

        return ResponseEntity
                .created(uriComponentsBuilder.path("/products/{id}").build(created.getCode()))
                .body(created);
    }

    @GetMapping("/products")
    public List<ProductDto> findAllProducts() {
        log.info("Request for all products");
        return productService.findAllProducts();
    }

    @GetMapping("/products/{code}")
    public ProductDto findProductByCode(@PathVariable String code) {
        log.info("Request for a product with code={}", code);
        return productService.findProductByCode(code);
    }

    @PutMapping("/products/{code}")
    public ProductDto updateProduct(@PathVariable String code,
                                    @RequestBody ProductDto update) {
        log.info("Request for updating product: {}", update);

        if (!code.equals(update.getCode())) {
            throw new BadRequestException("Product id and URI id don't match");
        }

        return productService.updateProduct(code, update);
    }

    @DeleteMapping("/products/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable String code) {
        log.info("Request for deleting product with code={}", code);
        productService.deleteProduct(code);
    }

    @GetMapping("/products/search")
    public List<ProductDto> searchProducts(@RequestParam @Size(min = 2) String query) {
        log.info("Request to search products: {}", query);
        return productService.search(query);
    }

    @GetMapping("/products/list")
    public List<ProductDto> findProductsByCodes(@RequestParam @NotEmpty List<String> code) {
        log.info("Request for products by code: {}", code);
        return productService.findAllByCodes(code);
    }
}
