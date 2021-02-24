package com.mikhailkarpov.shoppingcart.service;

import com.mikhailkarpov.shoppingcart.dto.ShoppingCart;
import com.mikhailkarpov.shoppingcart.dto.ShoppingCartItem;

import java.util.List;
import java.util.Optional;

public interface ShoppingCartService {

    void deleteCart(String cartId);

    ShoppingCart findCartById(String cartId);

    ShoppingCart saveCart(String cartId, List<ShoppingCartItem> items);
}
