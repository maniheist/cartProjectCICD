package com.cart_project.cartProject.exceptions;

public class InsufficientBalanceException extends RuntimeException{
    public InsufficientBalanceException (String message){
        super(message);
    }

    @Override
    public String getMessage(){
        return "Insufficient Balance";
    }

    @Override
    public String toString(){
        return "Insufficient Balance Please Check Your Bank Balance";
    }
}
