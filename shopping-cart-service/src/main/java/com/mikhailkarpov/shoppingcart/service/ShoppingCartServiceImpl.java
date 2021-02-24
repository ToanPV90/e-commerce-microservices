package com.mikhailkarpov.shoppingcart.service;

import com.mikhailkarpov.shoppingcart.dto.ShoppingCart;
import com.mikhailkarpov.shoppingcart.dto.ShoppingCartItem;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    //todo add cacheable annotations

    @Autowired
    private final RedisTemplate<String, ShoppingCart> redisTemplate;

    @Override
    public ShoppingCart saveCart(String cartId, List<ShoppingCartItem> items) {

        ShoppingCart shoppingCart = new ShoppingCart(cartId, items);
        redisTemplate.opsForValue().set(cartId, shoppingCart);
        return shoppingCart;
    }

    @Override
    public void deleteCart(String cartId) {
        redisTemplate.delete(cartId);
    }

    @Override
    public ShoppingCart findCartById(String cartId) {
        ShoppingCart shoppingCart = redisTemplate.opsForValue().get(cartId);

        if (shoppingCart == null) {
            shoppingCart = new ShoppingCart(cartId, Collections.emptyList());
        }

        return shoppingCart;
    }
}
