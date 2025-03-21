package com.cart_project.cartProject.models;


import com.cart_project.cartProject.entity.Cart;
import com.cart_project.cartProject.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor

public class UpdateResult {
    private final Cart cartItem;
    private final Product product;
    private final boolean success;


}

