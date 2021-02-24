package com.mikhailkarpov.shoppingcart.service;

import com.mikhailkarpov.shoppingcart.dto.ShoppingCart;
import com.mikhailkarpov.shoppingcart.dto.ShoppingCartItem;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    //todo add cacheable annotations

    @Autowired
    private final RedisTemplate<String, ShoppingCart> redisTemplate;

    @Override
    @CachePut(value = "shopping-cart", key = "#cartId", unless = "#root.args[1].isEmpty()")
    public ShoppingCart saveCart(String cartId, List<ShoppingCartItem> items) {

        ShoppingCart shoppingCart = new ShoppingCart(cartId, items);
        redisTemplate.opsForValue().set(cartId, shoppingCart);
        return shoppingCart;
    }

    @Override
    @CacheEvict(value = "shopping-cart", key = "#cartId")
    public void deleteCart(String cartId) {

        redisTemplate.delete(cartId);
    }

    @Override
    @Cacheable(value = "shopping-cart", key = "#cartId", unless = "#result == null")
    public Optional<ShoppingCart> findCartById(String cartId) {

        ShoppingCart shoppingCart = redisTemplate.opsForValue().get(cartId);
        return Optional.ofNullable(shoppingCart);
    }
}
