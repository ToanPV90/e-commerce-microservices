package com.mikhailkarpov.shoppingcart.controller;

import com.mikhailkarpov.shoppingcart.domain.CreateShoppingCartRequest;
import com.mikhailkarpov.shoppingcart.domain.Product;
import com.mikhailkarpov.shoppingcart.domain.ShoppingCartItem;
import com.mikhailkarpov.shoppingcart.entity.ShoppingCart;
import com.mikhailkarpov.shoppingcart.service.ProductService;
import com.mikhailkarpov.shoppingcart.service.ShoppingCartService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@AllArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final ProductService productService;

    @PostMapping("/shopping-cart")
    public ResponseEntity<List<Product>> createCart(@Valid @RequestBody CreateShoppingCartRequest request,
                                                    HttpSession session,
                                                    UriComponentsBuilder uriComponentsBuilder) {
        log.info("Request to create shopping cart: {}", request);

        ShoppingCart cart = shoppingCartService.createCart(session.getId(), request.getItems());
        List<Product> products = getProducts(cart);

        return ResponseEntity
                .created(uriComponentsBuilder.path("/shopping-cart").build().toUri())
                .body(products);
    }

    @GetMapping("/shopping-cart")
    public List<Product> getCart(HttpSession session) {
        log.info("Request for shopping cart with session_id={}", session.getId());

        ShoppingCart cart = shoppingCartService.findById(session.getId());
        return getProducts(cart);
    }

    @PutMapping("/shopping-cart")
    public List<Product> updateCart(@Valid @RequestBody CreateShoppingCartRequest request, HttpSession session) {
        log.info("Request to update shopping cart {}", request);

        ShoppingCart cart = shoppingCartService.updateCart(session.getId(), request.getItems());
        return getProducts(cart);
    }

    @DeleteMapping("/shopping-cart")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCart(HttpSession session) {
        log.info("Request to delete shopping cart with id = {}", session.getId());
        shoppingCartService.deleteCart(session.getId());

        session.invalidate();
        log.info("Session invalidated");
    }

    private List<Product> getProducts(ShoppingCart shoppingCart) {
        Map<String, Integer> quantityPerCode = shoppingCart.getItems().stream().collect(
                Collectors.toMap(ShoppingCartItem::getCode, ShoppingCartItem::getQuantity));

        List<Product> products = productService.getProductsByCodes(new ArrayList<>(quantityPerCode.keySet()));

        log.info("Got {} product(s):", products.size());
        products.forEach(product -> {
            String code = product.getCode();
            Integer quantity = quantityPerCode.get(code);

            product.setQuantity(quantity);
            log.info("\t{}", product);
        });

        return products;
    }
}
