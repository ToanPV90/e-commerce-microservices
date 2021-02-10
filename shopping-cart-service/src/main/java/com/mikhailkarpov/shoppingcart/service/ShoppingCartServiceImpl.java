package com.mikhailkarpov.shoppingcart.service;

import com.mikhailkarpov.shoppingcart.domain.ShoppingCartItem;
import com.mikhailkarpov.shoppingcart.entity.ShoppingCart;
import com.mikhailkarpov.shoppingcart.exception.ConflictException;
import com.mikhailkarpov.shoppingcart.exception.ResourceNotFoundException;
import com.mikhailkarpov.shoppingcart.repository.ShoppingCartRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    @Transactional
    public ShoppingCart createCart(String id, Collection<ShoppingCartItem> items) {

        if (shoppingCartRepository.existsById(id)) {
            String message = String.format("Shopping cart already exists");
            log.warn(message);
            throw new ConflictException(message);
        }

        ShoppingCart shoppingCart = shoppingCartRepository.save(new ShoppingCart(id, new HashSet<>(items)));

        log.info("Created {}", shoppingCart);
        return shoppingCart;
    }

    @Override
    @Transactional
    @Cacheable(value = "shopping-cart", key = "#id")
    public ShoppingCart findById(String id) {
        Optional<ShoppingCart> found = shoppingCartRepository.findById(id);

        if (!found.isPresent()) {
            String message = String.format("Shopping cart with id = %s not found", id);
            log.warn(message);
            throw new ResourceNotFoundException(message);
        }

        ShoppingCart shoppingCart = found.get();
        log.info("Found {}", shoppingCart);
        return shoppingCart;
    }

    @Override
    @Transactional
    @CachePut(value = "shopping-cart", key = "#id")
    public ShoppingCart updateCart(String id, Collection<ShoppingCartItem> items) {
        ShoppingCart shoppingCart = findById(id);
        shoppingCart.setItems(new HashSet<>(items));
        shoppingCart = shoppingCartRepository.save(shoppingCart);

        log.info("Updated {}", shoppingCart);
        return shoppingCart;
    }

    @Override
    @CacheEvict(value = "shopping-cart", key = "#id")
    @Transactional
    public void deleteCart(String id) {
        ShoppingCart shoppingCart = findById(id);
        shoppingCartRepository.deleteById(id);

        log.info("Deleted {}", shoppingCart);
    }
}
