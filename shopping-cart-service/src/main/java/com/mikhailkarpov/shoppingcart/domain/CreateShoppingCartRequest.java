package com.mikhailkarpov.shoppingcart.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateShoppingCartRequest {

    @NotNull
    @NotEmpty
    private List<ShoppingCartItem> items;
}
