package com.cart_project.cartProject.controller;

import com.cart_project.cartProject.entity.Payment;
import com.cart_project.cartProject.entity.User;
import com.cart_project.cartProject.exceptions.InsufficientBalanceException;
import com.cart_project.cartProject.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/payment")
public class PaymentController {

    @Autowired
    private final PaymentService paymentService;

    @PostMapping(value = "/buy")
    public ResponseEntity<String> checkOut(@RequestParam int userId,@RequestParam int checkOutId,
                                           @RequestParam int amount){
        if(paymentService.checkOut(userId,checkOutId,amount)){
            return ResponseEntity.ok("Transaction Successfully Completed");
        }

        throw new InsufficientBalanceException("Insufficient Balance");
    }

    @GetMapping(value="/paymentHistory/{userId}")
    public List<Payment> userHistory(@PathVariable int userId){
       return paymentService.userHistory(userId);
    }
}
