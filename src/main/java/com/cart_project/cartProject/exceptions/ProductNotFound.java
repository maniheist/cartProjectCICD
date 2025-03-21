package com.cart_project.cartProject.exceptions;

public class ProductNotFound extends RuntimeException{

    public ProductNotFound(String message){
        super(message);
    }
    @Override
    public String getMessage(){
        return "Product Not Found";
    }

    @Override
    public String toString(){
        return "Product Not Found";
    }
}
