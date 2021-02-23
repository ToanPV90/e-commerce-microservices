package com.mikhailkarpov.shoppingcart.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED) // for JSON mapping
@AllArgsConstructor
public class ShoppingCartItem implements Serializable {

    private static final long serialVersionUID = -7958014608926443785L;

    @NotBlank
    private String code;

    @NotNull
    @Size(min = 1)
    private Integer quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCartItem that = (ShoppingCartItem) o;
        return code.equals(that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return "ShoppingCartItem{" +
                "code='" + code + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
