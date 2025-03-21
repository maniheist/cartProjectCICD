package com.cart_project.cartProject.controller;

import com.cart_project.cartProject.annotations.AdminOnly;
import com.cart_project.cartProject.entity.Product;
import com.cart_project.cartProject.entity.User;
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
@RequestMapping(value = "/product")
public class ProductController {
    @Autowired
    private final ProductService productService;
    @Autowired
    private final LoginService loginService;

    @AdminOnly
    @PostMapping("/{userId}/add")
    public ResponseEntity<String> addProduct(@PathVariable int userId,@RequestBody Product product) {
        productService.addProduct(product);
        return ResponseEntity.ok("Product Added Successfully");
    }

    @AdminOnly
    @PutMapping("/update/{userId}/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int userId,@PathVariable int id, @RequestBody Product productDetails) {
        User user= loginService.getUserById(userId);
           productService.updateProduct(id, productDetails);
           return ResponseEntity.ok("Updated Successfully");
    }

    @AdminOnly
    @PatchMapping ("/updateProductPrice")
    public ResponseEntity<?> updateProductPrice(@RequestParam int userId,@RequestParam int productId,@RequestParam int productPrice){
        productService.updateProductPrice(productId,productPrice);
        return new ResponseEntity<>("Price Update Successfully",HttpStatus.OK);
    }

    @AdminOnly
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteProduct(@RequestParam int userId,@RequestParam int productId) {
        productService.deleteProduct(userId,productId);
        return ResponseEntity.ok("Product deleted successfully");
    }


    @GetMapping("/productId/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable int id) {
            Product product=productService.getProductById(id);
            return ResponseEntity.ok(product);
    }

    @GetMapping("/name/{productName}")
    public ResponseEntity<List<Product>> findByProductName(@PathVariable String productName){
        List<Product> products=productService.findByProductName(productName);
        return ResponseEntity.ok(products);
    }

    @GetMapping(value = "/view/{userId}")
    public ResponseEntity<List<Product>> viewProducts(@PathVariable int userId){
        return ResponseEntity.ok(productService.viewProducts(userId));
    }

    @GetMapping(value="/productPrice/{productId}")
    public ResponseEntity<?> productPrice(@PathVariable int productId){
        double newProductPrice=productService.findPriceById(productId);
        return new ResponseEntity<>(newProductPrice,HttpStatus.OK);
    }

}
