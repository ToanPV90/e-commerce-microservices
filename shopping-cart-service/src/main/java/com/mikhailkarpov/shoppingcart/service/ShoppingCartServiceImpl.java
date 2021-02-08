package com.mikhailkarpov.shoppingcart.service;

import com.mikhailkarpov.shoppingcart.domain.Product;
import com.mikhailkarpov.shoppingcart.domain.ShoppingCartItem;
import com.mikhailkarpov.shoppingcart.entity.ShoppingCart;
import com.mikhailkarpov.shoppingcart.exception.ResourceNotFoundException;
import com.mikhailkarpov.shoppingcart.repository.ShoppingCartRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductService productService;

    @Override
    public void createCart(String id, List<ShoppingCartItem> items) {
        ShoppingCart shoppingCart = shoppingCartRepository.save(new ShoppingCart(id, new HashSet<>(items)));
        log.info("Created {}", shoppingCart);
    }

    @Override
    @Transactional
    public List<Product> findById(String id) {
        Optional<ShoppingCart> found = shoppingCartRepository.findById(id);

        if (!found.isPresent()) {
            String message = String.format("Shopping cart with id = %s not found", id);
            log.warn(message);

            log.info("Returning empty products list");
            return Collections.emptyList();
        }

        ShoppingCart shoppingCart = found.get();

        Map<String, Integer> quantityPerCode = shoppingCart.getItems().stream().collect(
                Collectors.toMap(ShoppingCartItem::getCode, ShoppingCartItem::getQuantity));

        List<Product> products = productService.getProductsByCodes(new ArrayList<>(quantityPerCode.keySet()));
        log.info("Got {} product(s):", products.size());
        products.forEach(product -> {
            String code = product.getCode();
            Integer quantity = quantityPerCode.get(code);

            product.setQuantity(quantity);
            log.info("\t{}", product);
        });

        return products;
    }

    @Override
    public void deleteCart(String id) {
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findById(id);

        if (!shoppingCart.isPresent()) {
            String message = String.format("Shopping cart with id = %s not found", id);
            log.warn(message);
            throw new ResourceNotFoundException(message);
        }

        shoppingCartRepository.deleteById(id);
        log.info("Deleted {}", shoppingCart.get());
    }

}
