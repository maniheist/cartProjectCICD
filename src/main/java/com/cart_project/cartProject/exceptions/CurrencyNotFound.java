package com.cart_project.cartProject.exceptions;

public class CurrencyNotFound extends RuntimeException{
    public CurrencyNotFound(String message){
        super(message);
    }

    @Override
    public String getMessage(){
        return "Please Enter Valid Country Code";
    }
    @Override
    public String toString() {
        return "Please Enter Valid Country Code";
    }

}
