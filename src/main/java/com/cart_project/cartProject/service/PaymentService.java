package com.cart_project.cartProject.service;

import com.cart_project.cartProject.exceptions.UserNotFound;
import com.cart_project.cartProject.entity.Cart;
import com.cart_project.cartProject.entity.Payment;
import com.cart_project.cartProject.entity.User;
import com.cart_project.cartProject.repository.CartRepository;
import com.cart_project.cartProject.repository.LoginRepository;
import com.cart_project.cartProject.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.ReentrantLock;

@RequiredArgsConstructor
@Service
public class PaymentService {

    private final CartRepository cartRepository;

    private final LoginRepository loginRepository;

    private final PaymentRepository paymentRepository;

    private final LoginService loginService;

    private final CartService cartService;

    public boolean checkOut(int id,int checkOutId,double amount){

        final ReentrantLock lock=new ReentrantLock();
        CompletableFuture<User> userFuture=CompletableFuture.supplyAsync(()->loginRepository.findById(id).orElseThrow(()->new UserNotFound("User Not Found")));
        CompletableFuture<List<Cart>>cartListFuture=CompletableFuture.supplyAsync(()->cartRepository.findByUserId(id));

        CompletableFuture<Double> completableFutureTotalAmount=CompletableFuture.supplyAsync(()->
            cartService.calculateTotalAmount(id)
        );

        boolean paymentSuccess=CompletableFuture.allOf(userFuture,completableFutureTotalAmount)
                .thenApply(__->{
                    User user=userFuture.join();
                    double totalAmount=completableFutureTotalAmount.join();


                    if(totalAmount>amount){
                        return false;
                    }
                    processPayment(checkOutId, user.getCountry(), (amount - totalAmount), user);
                    return true;
                }).join();

        if(!paymentSuccess){
            return false;
            }

        CompletableFuture.allOf(cartListFuture).thenAccept(__->{
            List<Cart> cartProducts=cartListFuture.join();
            cartProducts.forEach(cart -> cartService.removeFromCartByCartId(cart.getId()));
        });
        return true;
    }

    @Transactional
    public void processPayment(int checkOutId, String currency, double finalAmount, User user) {
        Payment payment = new Payment(checkOutId, currency, finalAmount, user);
        paymentRepository.save(payment);
    }

    @Transactional
    public boolean addPayment(Payment payment){
        try{
            paymentRepository.save(payment);
            return true;
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }
    }

    @Transactional(readOnly = true)
    public List<Payment> userHistory(int userId){
        return loginService.getUserById(userId).getPaymentList();
    }

}