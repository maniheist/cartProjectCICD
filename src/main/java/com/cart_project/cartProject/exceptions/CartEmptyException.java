package com.cart_project.cartProject.exceptions;

public class CartEmptyException extends RuntimeException{

    public CartEmptyException(String message){
        super(message);
    }

    @Override
    public String getMessage(){
        return "Cart is Empty";
    }

    @Override
    public String toString() {
        return "CartEmpty";
    }
}
