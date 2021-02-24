package com.mikhailkarpov.shoppingcart.service;

import com.mikhailkarpov.shoppingcart.AbstractIntegrationTest;
import com.mikhailkarpov.shoppingcart.dto.ShoppingCart;
import com.mikhailkarpov.shoppingcart.dto.ShoppingCartItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCacheManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ShoppingCartServiceImplIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ShoppingCartService shoppingCartService;

    private final ShoppingCart emptyCart = new ShoppingCart(UUID.randomUUID().toString(), Collections.emptyList());
    private final ShoppingCart fullCart = new ShoppingCart(UUID.randomUUID().toString(), Arrays.asList(
            new ShoppingCartItem("abc", 3),
            new ShoppingCartItem("xyz", 4)
    ));

    @AfterEach
    void clearCache() {
        cacheManager.getCache("shopping-cart").clear();

        assertNull(getFromCache(fullCart.getId()));
        assertNull(getFromCache(emptyCart.getId()));
    }

    @Test
    void contextLoads() {
        assertNotNull(cacheManager);
        assertNotNull(shoppingCartService);
        assertTrue(cacheManager instanceof RedisCacheManager);
    }

    @Test
    void givenFullCart_whenSaveCart_thenSavedAndCached() {
        //when
        ShoppingCart saved = shoppingCartService.saveCart(fullCart.getId(), fullCart.getItems());

        //then
        assertEquals(fullCart.getId(), saved.getId());
        assertIterableEquals(fullCart.getItems(), saved.getItems());

        //...and
        ShoppingCart cached = getFromCache(fullCart.getId());

        assertNotNull(cached);
        assertEquals(fullCart.getId(), cached.getId());
        assertIterableEquals(fullCart.getItems(), cached.getItems());
    }

    @Test
    void givenEmptyCart_whenSaveCart_thenSavedAndNotCached() {
        //when
        ShoppingCart saved = shoppingCartService.saveCart(emptyCart.getId(), emptyCart.getItems());

        //then
        assertEquals(emptyCart.getId(), saved.getId());
        assertIterableEquals(emptyCart.getItems(), saved.getItems());
        assertNull(getFromCache(emptyCart.getId()));
    }

    @Test
    void givenEmptyCache_whenDeleteCart_thenNothing() {
        //given
        String key = UUID.randomUUID().toString();
        assertNull(getFromCache(key));

        //when
        shoppingCartService.deleteCart(key);

        //then
        assertNull(getFromCache(key));
    }

    @Test
    void givenCachedShoppingCart_whenDeleteCart_thenEvicted() {
        //given
        putInCache(fullCart);

        //when
        shoppingCartService.deleteCart(fullCart.getId());

        //then
        assertNull(getFromCache(fullCart.getId()));
    }

    @Test
    void givenEmptyCache_whenFindById_thenNotFoundAndCacheIsEmpty() {
        String key = UUID.randomUUID().toString();
        Optional<ShoppingCart> found = shoppingCartService.findCartById(key);

        assertFalse(found.isPresent());
        assertNull(getFromCache(key));
    }

    @Test
    void givenShoppingCartInCache_whenFindById_thenFoundAndStillCached() {
        //given
        putInCache(fullCart);
        assertNotNull(getFromCache(fullCart.getId()));

        //when
        Optional<ShoppingCart> found = shoppingCartService.findCartById(fullCart.getId());

        //then
        assertTrue(found.isPresent());
        assertEquals(fullCart.getId(), found.get().getId());
        assertIterableEquals(fullCart.getItems(), found.get().getItems());
        assertNotNull(getFromCache(fullCart.getId()));
    }

    private void putInCache(ShoppingCart shoppingCart) {
        cacheManager
                .getCache("shopping-cart")
                .put(shoppingCart.getId(), shoppingCart);
    }

    private ShoppingCart getFromCache(String key) {
        return cacheManager
                .getCache("shopping-cart")
                .get(key, ShoppingCart.class);
    }
}