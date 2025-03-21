package com.cart_project.cartProject.controller;

import com.cart_project.cartProject.entity.Product;
import com.cart_project.cartProject.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/search")
public class SearchController {

    @Autowired
    private final SearchService searchService;

    @GetMapping(value = "/product")
    public ResponseEntity<List<Product>> productWithInPriceRanges(@RequestParam int userId,@RequestParam String productName, @RequestParam double leastPrice, @RequestParam double highPrice){
        try {
            return ResponseEntity.ok(searchService.productWithInPriceRange(userId,productName,leastPrice,highPrice));

        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping(value = "/{userId}/startsWith/{productName}")
    public ResponseEntity<List<Product>> allProductsThatStartWith(@PathVariable int userId,@PathVariable String productName){
        return ResponseEntity.ok(searchService.allProductsThatStartWith(userId,productName));
    }

    @GetMapping(value = "/lowPriceProducts")
    public ResponseEntity<Page<Product>> leastPriceProductsFiltering(@RequestParam String productName,@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "2") int size){
        return new ResponseEntity<>(searchService.leastPriceProductsFiltering(productName,page,size),HttpStatus.OK);
    }

    @GetMapping(value = "/highPriceProducts")
    public ResponseEntity<Page<Product>> highPriceProductsFiltering(@RequestParam String productName,@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "2") int size){
        return new ResponseEntity<>(searchService.highPricesProductsFiltering(productName,page,size),HttpStatus.OK);
    }


}
