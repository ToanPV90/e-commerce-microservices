package com.mikhailkarpov.shoppingcart.controller;

import com.mikhailkarpov.shoppingcart.dto.ApiErrorResponse;
import com.mikhailkarpov.shoppingcart.dto.ShoppingCart;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
public class ShoppingCartController {

    private static final String SHOPPING_CART_ATTRIBUTE = "cart";

    @GetMapping("/shopping-cart")
    public ShoppingCart findCart(HttpSession session) {

        log.info("Session_id={}: request for a shopping cart", session.getId());

        ShoppingCart cart = (ShoppingCart) session.getAttribute(SHOPPING_CART_ATTRIBUTE);

        if (cart == null) {
            cart = new ShoppingCart();
            session.setAttribute(SHOPPING_CART_ATTRIBUTE, cart);
        }

        return cart;
    }

    @PostMapping("/shopping-cart")
    public ResponseEntity<Object> addItem(@Valid @RequestBody ShoppingCart cart,
                                          HttpSession session,
                                          UriComponentsBuilder uriComponentsBuilder) {

        log.info("Session_id={}: request to create {}", session.getId(), cart);

        ShoppingCart existingCart = (ShoppingCart) session.getAttribute(SHOPPING_CART_ATTRIBUTE);

        if (existingCart != null) {
            ApiErrorResponse body = new ApiErrorResponse("Shopping cart already exist");
            return new ResponseEntity<>(body, HttpStatus.CONFLICT);
        }

        session.setAttribute(SHOPPING_CART_ATTRIBUTE, cart);

        return ResponseEntity
                .created(uriComponentsBuilder.path("/shopping-cart").build().toUri())
                .body(cart);
    }

    @PutMapping("/shopping-cart")
    public ShoppingCart updateCart(@Valid @RequestBody ShoppingCart update,
                                   HttpSession session) {

        log.info("Session_id={}: request to update {}", session.getId(), update);

        session.setAttribute(SHOPPING_CART_ATTRIBUTE, update);
        return update;
    }

    @DeleteMapping("/shopping-cart")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(HttpSession session) {

        log.info("Session_id={}: request to clear shopping cart", session.getId());

        session.removeAttribute(SHOPPING_CART_ATTRIBUTE);
    }
}
