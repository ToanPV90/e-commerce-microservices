package com.mikhailkarpov.shoppingcart.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mikhailkarpov.shoppingcart.domain.ShoppingCartItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("ShoppingCart")
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = -2335232674226102709L;

    @Id
    @JsonIgnore
    private String id;

    @NotNull
    @NotEmpty
    private Set<ShoppingCartItem> items = new HashSet<>();
}
