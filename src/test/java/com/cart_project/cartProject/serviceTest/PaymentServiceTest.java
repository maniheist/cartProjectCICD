package com.cart_project.cartProject.serviceTest;

import com.cart_project.cartProject.entity.Cart;
import com.cart_project.cartProject.entity.Payment;
import com.cart_project.cartProject.entity.Product;
import com.cart_project.cartProject.entity.User;
import com.cart_project.cartProject.repository.CartRepository;
import com.cart_project.cartProject.repository.LoginRepository;
import com.cart_project.cartProject.repository.PaymentRepository;
import com.cart_project.cartProject.repository.ProductRepository;
import com.cart_project.cartProject.service.*;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
@ExtendWith(MockitoExtension.class)

public class PaymentServiceTest {
    private static Payment payment;
    private static User user;
    private static Cart cart;
    private static Product product;
    private static List<Cart> cartList;
    private static List<Payment> paymentList;
    @Mock
    LoginRepository loginRepository;
    @Mock
    CartRepository cartRepository;
    @Mock
    PaymentRepository paymentRepository;
    @Mock
    ProductService productService;
    @Mock
    CurrencyService currencyService;
    @Mock
    CartService cartService;
    @Mock
    LoginService loginService;
    @InjectMocks
    PaymentService paymentService;
    @BeforeAll
    public static void beforeAll(){
        payment=new Payment(1,"USD",10000,new User(1,"mani","kms@gmail.com","kdwqw","user","USA",null));
        paymentList=Arrays.asList(new Payment(1,"USD",10000,new User(1,"mani","kms@gmail.com","kdwqw","user","USA",null)));

        user=new User(1,"mani","kms@gmail.com","kdwqw","user","USA",null);
        product=new Product(1,"wire","Best One",12,30);
        cartList= Arrays.asList(new Cart(1,1,1,19));

    }
    @AfterAll
    public static void afterAll(){
        payment=null;
    }

    @Test
    public void checkOutTest(){
        Mockito.when(loginRepository.findById(1)).thenReturn(Optional.of(user));
        Mockito.when(cartRepository.findByUserId(1)).thenReturn(cartList);
        Mockito.when(paymentRepository.save(Mockito.any(Payment.class))).thenReturn(payment);
        Mockito.doNothing().when(cartService).removeFromCartByCartId(cartList.get(0).getId());
        boolean flag=paymentService.checkOut(1,1,10000);
        Assertions.assertTrue(flag);
        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        Mockito.verify(paymentRepository).save(paymentCaptor.capture());
        Payment savedPayment = paymentCaptor.getValue();

        Assertions.assertEquals(10000,savedPayment.getAmount());
    }

    @Test
    public void userHistoryTest(){
        User mockitoUser=Mockito.mock(User.class);
        Mockito.when(loginService.getUserById(1)).thenReturn(mockitoUser);
        Mockito.when(mockitoUser.getPaymentList()).thenReturn(paymentList);
        List<Payment> payments=paymentService.userHistory(1);
        Assertions.assertEquals(1,payments.size());
    }
}
