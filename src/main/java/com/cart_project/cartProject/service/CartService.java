package com.cart_project.cartProject.service;

import com.cart_project.cartProject.exceptions.CartEmptyException;
import com.cart_project.cartProject.entity.Cart;
import com.cart_project.cartProject.entity.Product;
import com.cart_project.cartProject.entity.User;
import com.cart_project.cartProject.models.UpdateResult;
import com.cart_project.cartProject.repository.CartRepository;
import com.cart_project.cartProject.repository.LoginRepository;
import com.cart_project.cartProject.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.CacheManager;

import java.util.Currency;
import java.util.List;
import java.util.concurrent.CompletableFuture;
@RequiredArgsConstructor
@Service
public class CartService {
    private final CartRepository cartRepository;

    private final ProductService productService;

    private final ProductRepository productRepository;

    private final CurrencyService currencyService;

    private final CountryService countryService;

    private final CacheManager cacheManager;

    private final LoginRepository loginRepository;

    private final LoginService loginService;


    public boolean addToCart(int userId,Cart cart) {
        CompletableFuture<Cart> cartFuture=CompletableFuture.supplyAsync(()->
                cartRepository.findByUserIdAndProductId(cart.getUserId(), cart.getProductId()).orElse(null)
        );
        CompletableFuture<Product> productFuture=CompletableFuture.supplyAsync(()->
                productService.getProductById(cart.getProductId()));
        CompletableFuture<UpdateResult> addFuture=cartFuture.thenCombine(productFuture,(exsist_cart,product)->{
            if (product.getStock() < cart.getQuantity()){
                return new UpdateResult(null,null,false);
            }
            if(exsist_cart!=null){
                exsist_cart.setQuantity(exsist_cart.getQuantity() + cart.getQuantity());
            }
            else{
                exsist_cart=cart;
            }
            product.setStock(product.getStock() - cart.getQuantity());
            return new UpdateResult(exsist_cart,product,true);
        });
        UpdateResult saveFuture=addFuture.join();
        if(!saveFuture.isSuccess()){
            return false;
        }
        CartAndProductSave(saveFuture.getCartItem(),saveFuture.getProduct());

        return true;
    }

    @Transactional
    public void CartAndProductSave(Cart cart,Product product){
        cartRepository.save(cart);
        productService.updateProduct(product.getId(),product);
    }

    public List<Cart> viewCart(int userId) {
        CompletableFuture<List<Cart>> completableFutureViewCart=CompletableFuture.supplyAsync(()->{
            return cartRepository.findByUserId(userId);
        });
        return completableFutureViewCart.join();

    }

    public boolean updateCartQuantity(int userId, int productId, int quantity) {
        CompletableFuture<Cart> cartFuture = CompletableFuture.supplyAsync(() ->
                cartRepository.findByUserIdAndProductId(userId, productId)
                        .orElseThrow(() -> new CartEmptyException("Cart Of User "+userId+" Is Empty"))
        );

        CompletableFuture<Product> productFuture = CompletableFuture.supplyAsync(() ->
                productService.getProductById(productId)
        );

        CompletableFuture<UpdateResult> updateFuture = cartFuture.thenCombine(productFuture, (cartItem, product) -> {
            int currentCartQuantity = cartItem.getQuantity();
            int stockDifference = quantity - currentCartQuantity;

            if (stockDifference > 0) {
                if (product.getStock() < stockDifference) {
                    return new UpdateResult(null,null,false);
                }
                product.setStock(product.getStock() - stockDifference);
            } else if (stockDifference < 0) {
                product.setStock(product.getStock() + Math.abs(stockDifference));
            }

            cartItem.setQuantity(quantity);
            return new UpdateResult(cartItem,product,true);
        });

        UpdateResult saveFuture=updateFuture.join();
        if(!saveFuture.isSuccess()){
            return false;
        }
        CartAndProductSave(saveFuture.getCartItem(),saveFuture.getProduct());
        return true;
    }

    @Transactional
    public void removeFromCart(int userId, int productId) {

        CompletableFuture.supplyAsync(()-> {
            return cartRepository.findByUserIdAndProductId(userId, productId).orElseThrow(() -> new CartEmptyException("User " + userId + " Cart Not Found"));
        }).thenAccept(cartItem->{
            Product product=productService.getProductById(productId);
            product.setStock(product.getStock()+cartItem.getQuantity());
            productService.updateProduct(productId,product);
            cartRepository.delete(cartItem);
        }).join();
    }

    @Transactional
    public void removeFromCartByCartId(int cartId){

        CompletableFuture.runAsync(()->{
            cartRepository.deleteById(cartId);
        });
    }

    public double calculateTotalAmount(int userId){
        CompletableFuture<List<Cart>> futureTotalAmount=CompletableFuture.supplyAsync(()->cartRepository.findByUserId(userId));
        User user=loginService.getUserById(userId);
        return futureTotalAmount.thenApply((cartItems)->{
            double totalAmount=0;
            double currencyConversionAmount=1;

            if (user!=null && user.getCountry() != null && !user.getCountry().isEmpty()) {
                Currency currencyConversionCode = countryService.changeCountryToCurrencyCode(user.getCountry());
                currencyConversionAmount = currencyService.changeToBaseCurrency(currencyConversionCode.toString().toLowerCase());
            }
            for (Cart item : cartItems) {
                double price = productService.findPriceById(item.getProductId());
                totalAmount += price * item.getQuantity();

            }
            return totalAmount*currencyConversionAmount;
        }).join();
    }
}
