package com.cart_project.cartProject.exceptions;

public class ItemNotFound extends RuntimeException{

    public ItemNotFound(String message){
        super(message);
    }
    @Override
    public String getMessage(){
        return "Item Not Found";
    }

    @Override
    public String toString() {
        return "Item Not Found";
    }
}
