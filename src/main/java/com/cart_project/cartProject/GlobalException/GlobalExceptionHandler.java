package com.cart_project.cartProject.GlobalException;
import com.cart_project.cartProject.exceptions.*;
import com.cart_project.cartProject.models.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice

public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFound.class)
    public ResponseEntity<?> HandleUserNotFoundException(UserNotFound exception){
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                exception.getMessage(),
                "User Not Found"
        );        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductNotFound.class)
    public ResponseEntity<?> HandleProductNotFoundException(ProductNotFound exception){
        ErrorResponse errorResponse=new ErrorResponse(
                LocalDateTime.now(),
                exception.getMessage(),
                "Product Not Found"
        );
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ItemNotFound.class)
    public ResponseEntity<?> HandleItemNotFoundException(ItemNotFound exception){
        ErrorResponse errorResponse=new ErrorResponse(
                LocalDateTime.now(),
                exception.getMessage(),
                "Item Not Found"
        );
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CartEmptyException.class)
    public ResponseEntity<?> HandleEmptyCartException(CartEmptyException exception){
        ErrorResponse errorResponse=new ErrorResponse(
                LocalDateTime.now(),
                exception.getMessage(),
                "Cart Empty"
        );
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AdminNotFoundException.class)
        public ResponseEntity<?> HandleAdminNotFoundException(AdminNotFoundException exception){
            ErrorResponse errorResponse=new ErrorResponse(
                    LocalDateTime.now(),
                    exception.getMessage(),
                    "Only Admin Can Do This Operation"
            );
            return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
        }

    @ExceptionHandler(CurrencyNotFound.class)
        public ResponseEntity<?> HandleCurrencyNotFoundException(CurrencyNotFound exception){
        ErrorResponse errorResponse=new ErrorResponse(
                LocalDateTime.now(),
                exception.getMessage(),
                "Enter Valid Country Code"
        );
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
        public ResponseEntity<?> HandleInsufficientBalanceException(InsufficientBalanceException exception){
        ErrorResponse errorResponse=new ErrorResponse(
                LocalDateTime.now(),
                exception.getMessage(),
                "Insufficient Balance Please Contact Your Bank"
        );
        return new ResponseEntity<>(errorResponse,HttpStatus.PAYMENT_REQUIRED);
    }

    }




