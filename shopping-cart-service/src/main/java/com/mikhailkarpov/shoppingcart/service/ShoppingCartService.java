package com.mikhailkarpov.shoppingcart.service;

import com.mikhailkarpov.shoppingcart.domain.ShoppingCartItem;
import com.mikhailkarpov.shoppingcart.entity.ShoppingCart;

import java.util.Collection;

public interface ShoppingCartService {

    ShoppingCart createCart(String id, Collection<ShoppingCartItem> items);

    ShoppingCart findById(String id);

    ShoppingCart updateCart(String id, Collection<ShoppingCartItem> items);

    void deleteCart(String id);

}
