package com.cart_project.cartProject.serviceTest;

import com.cart_project.cartProject.entity.Cart;
import com.cart_project.cartProject.entity.Product;
import com.cart_project.cartProject.entity.User;
import com.cart_project.cartProject.repository.CartRepository;
import com.cart_project.cartProject.repository.ProductRepository;
import com.cart_project.cartProject.service.*;
import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@ExtendWith(MockitoExtension.class)

public class CartServiceTest {

    private static Cart cart;
    private static Product product;
    private static User user;
    private static List<Cart> cartList;
    @Mock
    CartRepository cartRepository;
    @Mock
    ProductService productService;
    @Mock
    LoginService loginService;
    @Mock
    CountryService countryService;
    @Mock
    CurrencyService currencyService;
    @Mock
    ProductRepository productRepository;
    @InjectMocks
    CartService cartService;

    @BeforeAll
    public static void beforeAll(){
        cart=new Cart();
        cart.setQuantity(10);
        cart.setProductId(1);
        cart.setId(1);
        cart.setUserId(1);
        product=new Product();
        product.setId(2);
        product.setPrice(12);
        product.setStock(11);
        cartList= Arrays.asList(new Cart(1,1,2,2));
        user=new User(1,"mani","kms@gmail.com","kdwqw","user","India",null);

    }

    @AfterAll
    public static void afterAll(){
        cart=null;
    }



    @Test
    public void CartAndProductSaveTest(){
        Mockito.when(cartRepository.save(cart)).thenReturn(cart);
        Mockito.when(productService.updateProduct(product.getId(), product)).thenReturn(product);
        cartService.CartAndProductSave(cart,product);
        Mockito.verify(cartRepository, Mockito.times(1)).save(cart);

    }

    @Test
    public void viewCartTest(){
        Mockito.when(cartRepository.findByUserId(1)).thenReturn(List.of(cart));
        List<Cart> c1=cartService.viewCart(1);
        Assertions.assertFalse(c1.isEmpty());
        Assertions.assertEquals(1,c1.get(0).getUserId());
    }

    @Test
    public void addCartTest(){
        Mockito.when(cartRepository.findByUserIdAndProductId(1,1)).thenReturn(Optional.of(cart));
        Mockito.when(productService.getProductById(1)).thenReturn(product);

        boolean dec=cartService.addToCart(cart.getId(), cart);
        Assertions.assertTrue(dec);
    }

    @Test
    public void UpdateCartTest(){
        Mockito.when(cartRepository.findByUserIdAndProductId(1,1)).thenReturn(Optional.ofNullable(cart));
        Mockito.when(productService.getProductById(1)).thenReturn(product);

        boolean res=cartService.updateCartQuantity(1,1,7);
        Assertions.assertTrue(res);
        Assertions.assertEquals(7,cart.getQuantity());
    }

    @Test
    public void totalAmountTest(){
        List<CountryCode> code=CountryCode.findByName(user.getCountry());
        Mockito.when(cartRepository.findByUserId(1)).thenReturn(cartList);
        Mockito.when(loginService.getUserById(1)).thenReturn(user);
        Mockito.when(countryService.changeCountryToCurrencyCode(user.getCountry())).thenReturn(code.get(0).getCurrency());
        Mockito.when(currencyService.changeToBaseCurrency(code.get(0).getCurrency().toString().toLowerCase())).thenReturn(110.00);
        Mockito.when(productService.findPriceById(2)).thenReturn(product.getPrice());
        double totalAmount=cartService.calculateTotalAmount(1);
        Assertions.assertEquals(2640,totalAmount);

    }



}






//    @BeforeEach
//    public void beforeEach(){
//        cart=new Cart();
//        cart.setQuantity(10);
//        cart.setProductId(1);
//        cart.setId(1);
//        cart.setUserId(1);
//        product=new Product();
//        product.setStock(11);
//    }
//    @Test
//    public void deleteCartTest() throws ExecutionException, InterruptedException {
//        Mockito.when(cartRepository.findByUserIdAndProductId(1,1)).thenReturn(Optional.of(cart));
//
//        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> cartService.removeFromCart(1,1));
//        future.get();
//        Mockito.verify(cartRepository, Mockito.times(1)).delete(Mockito.any(Cart.class));
//    }