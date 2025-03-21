package com.cart_project.cartProject.controller;

import com.cart_project.cartProject.entity.Product;
import com.cart_project.cartProject.entity.User;
import com.cart_project.cartProject.exceptions.CartEmptyException;
import com.cart_project.cartProject.entity.Cart;
import com.cart_project.cartProject.exceptions.ProductNotFound;
import com.cart_project.cartProject.exceptions.UserNotFound;
import com.cart_project.cartProject.service.CartService;
import com.cart_project.cartProject.service.LoginService;
import com.cart_project.cartProject.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/cart")
public class CartController {
    @Autowired
    private final CartService cartService;

    private final LoginService loginService;

    private final ProductService productService;

    @PostMapping(value = "/add")
    public ResponseEntity<String> addToCart(@RequestBody Cart cart){
        User user=loginService.getUserById(cart.getUserId());
        if(user==null){
            throw new UserNotFound("User "+cart.getUserId()+" Not Found");
        }
        Product product=productService.getProductById(cart.getProductId());
        if(product==null){
            throw new ProductNotFound("Product "+cart.getProductId()+" Not Found");
        }
        if(cartService.addToCart(cart.getId(),cart)){
            return ResponseEntity.ok("Added To Cart Successfully");
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Out Of Stock");
        }
    }



    @PutMapping(value = "/updateQuantity")
    public ResponseEntity<String> updateCartQuantity(@RequestParam int userId, @RequestParam int productId, @RequestParam int quantity) {
        if(cartService.updateCartQuantity(userId, productId, quantity)){
            return ResponseEntity.ok("Cart Updated Successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Out Of Stock");
    }

    @GetMapping(value="/view/{userId}")
    public ResponseEntity<List<Cart>> viewCart(@PathVariable int userId) {
        List<Cart> cartList=cartService.viewCart(userId);
        if(cartList.isEmpty()){
            throw new CartEmptyException("User "+userId+" Cart is Empty");
        }
        return ResponseEntity.ok(cartList);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromCart(@RequestParam int userId, @RequestParam int productId) {

        cartService.removeFromCart(userId, productId);
        return ResponseEntity.ok("Product removed from cart");
    }

    @GetMapping("/totalAmount/{userId}")
    public ResponseEntity<?> calculateTotalAmount(@PathVariable int userId){
        return new ResponseEntity<>(cartService.calculateTotalAmount(userId),HttpStatus.OK);
    }
}
