package com.mikhailkarpov.shoppingcart.controller;

import com.mikhailkarpov.shoppingcart.domain.CreateShoppingCartRequest;
import com.mikhailkarpov.shoppingcart.domain.Product;
import com.mikhailkarpov.shoppingcart.domain.ShoppingCartItem;
import com.mikhailkarpov.shoppingcart.service.ShoppingCartService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    @PostMapping("/shopping-cart")
    @ResponseStatus(HttpStatus.CREATED)
    public void createCart(@Valid @RequestBody CreateShoppingCartRequest request, HttpSession session) {
        List<ShoppingCartItem> items = request.getItems();
        log.info("Request with session_id={} to create shopping cart with {} item(s)", session.getId(), items.size());
        shoppingCartService.createCart(session.getId(), items);
    }

    @GetMapping("/shopping-cart")
    public List<Product> getCart(HttpSession session) {
        log.info("Request for shopping cart with session_id={}", session.getId());
        return shoppingCartService.findById(session.getId());
    }

    @DeleteMapping("/shopping-cart")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCart(HttpSession session) {
        log.info("Request to delete shopping cart with session_id={}", session.getId());
        shoppingCartService.deleteCart(session.getId());

        session.invalidate();
        log.info("Session invalidated");
    }
}
