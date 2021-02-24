package com.mikhailkarpov.shoppingcart.controller;

import com.mikhailkarpov.shoppingcart.dto.ShoppingCart;
import com.mikhailkarpov.shoppingcart.dto.ShoppingCartItem;
import com.mikhailkarpov.shoppingcart.service.ShoppingCartService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class ShoppingCartController {

    //todo return products lists

    private final ShoppingCartService shoppingCartService;

    @GetMapping("/shopping-cart")
    public ShoppingCart findCart(@CookieValue(name = "cartId", required = false) String cartId,
                                 HttpSession session,
                                 HttpServletResponse response) {

        if (cartId == null) {
            cartId = session.getId();
        }
        log.info("Request for a shopping cart by cart_id={}", cartId);

        ShoppingCart shoppingCart = shoppingCartService
                .findCartById(cartId)
                .orElse(new ShoppingCart(cartId, Collections.emptyList()));

        addCartCookie(response, cartId);

        return shoppingCart;
    }

    @PostMapping("/shopping-cart")
    public ResponseEntity<ShoppingCart> createCart(@CookieValue(name = "cartId", required = false) String cartId,
                                                   @Valid @RequestBody List<ShoppingCartItem> items,
                                                   HttpSession session,
                                                   HttpServletResponse response,
                                                   UriComponentsBuilder uriComponentsBuilder) {

        if (cartId == null) {
            cartId = session.getId();
        }
        log.info("Request to save a shopping cart: cartId={}, items={}", cartId, items);

        ShoppingCart shoppingCart = shoppingCartService.saveCart(cartId, items);
        addCartCookie(response, cartId);

        return ResponseEntity
                .created(uriComponentsBuilder.path("/shopping-cart").build().toUri())
                .body(shoppingCart);
    }

    @DeleteMapping("/shopping-cart")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(@CookieValue(value = "cartId", required = false) String cartId,
                          HttpServletResponse response) {

        log.info("Request to delete a shopping cart with id={}", cartId);
        if (cartId != null) {
            shoppingCartService.deleteCart(cartId);

            Cookie cookie = new Cookie("cartId", cartId);
            cookie.setMaxAge(-1);

            response.addCookie(cookie);
        }
    }

    public void addCartCookie(HttpServletResponse response, String cartId) {

        Cookie cookie = new Cookie("cartId", cartId);
        cookie.setMaxAge(7 * 24 * 60 * 60);

        log.info("Adding {}", cookie);
        response.addCookie(cookie);
    }
}
