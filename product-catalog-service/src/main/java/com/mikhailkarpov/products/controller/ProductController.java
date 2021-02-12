package com.mikhailkarpov.products.controller;

import com.mikhailkarpov.products.dto.ProductDto;
import com.mikhailkarpov.products.exception.BadRequestException;
import com.mikhailkarpov.products.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @PostMapping("/categories/{id}/products")
    public ResponseEntity<ProductDto> createProduct(@PathVariable("id") Long categoryId,
                                                    @Valid @RequestBody ProductDto product,
                                                    UriComponentsBuilder uriComponentsBuilder) {

        log.info("Request to create product in category '{}': {}", categoryId, product);
        ProductDto created = productService.createProduct(categoryId, product);

        return ResponseEntity
                .created(uriComponentsBuilder.path("/products/{code}").build(created.getCode()))
                .body(created);
    }

    @GetMapping("/categories/{id}/products")
    public Iterable<ProductDto> findProductsByCategoryId(@PathVariable("id") Long categoryId) {

        log.info("Request for products by category_id = {}", categoryId);
        return productService.findAllByCategoryId(categoryId);
    }

    @GetMapping("/products/{code}")
    public ProductDto findProductByCode(@PathVariable String code) {

        log.info("Request for a product with code={}", code);
        return productService.findProductByCode(code);
    }

    @PutMapping("/products/{code}")
    public ProductDto updateProduct(@PathVariable String code, @Valid @RequestBody ProductDto update) {

        log.info("Request to update product: {}", update);

        if (!code.equals(update.getCode())) {
            throw new BadRequestException("URI code and product code don't match");
        }
        return productService.updateProduct(code, update);
    }

    @DeleteMapping("/products/{code}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable String code) {

        log.info("Request to delete product '{}'", code);
        productService.deleteProduct(code);
    }

    @GetMapping("/products/search")
    public Iterable<ProductDto> searchProducts(@RequestParam @Size(min = 2) String query) {

        log.info("Request to search products: {}", query);
        return productService.search(query);
    }

    @GetMapping("/products/list")
    public Iterable<ProductDto> findProductsByCodes(@RequestParam @NotEmpty List<String> code) {

        log.info("Request for products by code: {}", code);
        return productService.findAllByCodes(code);
    }
}
