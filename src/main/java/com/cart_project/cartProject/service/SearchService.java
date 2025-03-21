package com.cart_project.cartProject.service;

import com.cart_project.cartProject.entity.Product;
import com.cart_project.cartProject.entity.User;
import com.cart_project.cartProject.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
@RequiredArgsConstructor
@Service
public class SearchService {
    private final ProductService productService;

    private final LoginService loginService;

    private final ProductRepository productRepository;

    private final CountryService countryService;

    private final CurrencyService currencyService;

    @Cacheable(value = "products")
    public List<Product> productWithInPriceRange(int userId,String productName, double leastPrice, double highPrice) {

        return CompletableFuture.supplyAsync(()->{
            return productService.findByProductName(productName);
        }).thenApply((productNames)->{
            User user=loginService.getUserById(userId);

            Currency currencyConversionCode = countryService.changeCountryToCurrencyCode(user.getCountry());
            double currencyConversionAmount = currencyService.changeToBaseCurrency(currencyConversionCode.toString().toLowerCase());
            System.out.println(currencyConversionAmount);
            return productNames.stream().peek(p->p.setPrice(currencyConversionAmount*p.getPrice())).collect(Collectors.toList());
        }).thenApply(productsList->{

            return productsList.stream().filter(p->p.getPrice()>leastPrice && p.getPrice()<highPrice).collect(Collectors.toList());
        }).join();
    }

    public List<Product> allProductsThatStartWith(int userId,String productName) {

        return CompletableFuture.supplyAsync(()->{
            return productService.viewProducts(userId);
        }).thenApply(productsList->{
            return productsList.stream().filter(p->p.getName().toLowerCase().startsWith(productName.toLowerCase())).collect(Collectors.toList());
        }).join();
    }

    public Page<Product> leastPriceProductsFiltering(String productName, int page, int size){
        Pageable pageable=  PageRequest.of(page,size, Sort.by("price").ascending());
        return productRepository.findByNameContainingIgnoreCase(pageable,productName);
    }

    public Page<Product> highPricesProductsFiltering(String productName,int page,int size){
        Pageable pageable= PageRequest.of(page,size,Sort.by("price").descending());
        return productRepository.findByNameContainingIgnoreCase(pageable,productName);
    }


}



