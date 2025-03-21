package com.cart_project.cartProject.exceptions;

public class UserNotFound extends RuntimeException{

    public UserNotFound(String message){
        super(message);
    }
    @Override
    public String getMessage(){
        return "User Not Found";
    }

    @Override
    public String toString(){
        return "User Not Found";
    }

}
