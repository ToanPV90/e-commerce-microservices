package com.mikhailkarpov.shoppingcart.service;

import com.mikhailkarpov.shoppingcart.domain.Product;
import com.mikhailkarpov.shoppingcart.domain.ShoppingCartItem;

import java.util.List;

public interface ShoppingCartService {

    void createCart(String id, List<ShoppingCartItem> items);

    List<Product> findById(String id);

    void deleteCart(String id);
}
