package com.cart_project.cartProject.service;

import com.cart_project.cartProject.entity.User;
import com.cart_project.cartProject.exceptions.ProductNotFound;
import com.cart_project.cartProject.entity.Product;
import com.cart_project.cartProject.repository.ProductRepository;
import com.neovisionaries.i18n.CountryCode;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final LoginService loginService;

    private final CountryService countryService;

    private final CurrencyService currencyService;

    @Transactional
    @Caching(
            put = {
                    @CachePut(value = "products", key = "#product.id"),
                    @CachePut(value = "productId", key = "#product.id")
            },
            evict = {
                    @CacheEvict(value = "allProducts", allEntries = true),
                    @CacheEvict(value = "findProductByNames", key = "#product.name",allEntries = true),
                    @CacheEvict(value = "productsStartWith",allEntries = true)
            }
    )

    public Product addProduct(Product product) {
        try {
           Product newProduct= productRepository.save(product);
           return newProduct;
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return null;
    }


    @Transactional
    @Caching(
            put = {
                    @CachePut(value = "products", key = "#id"),
                    @CachePut(value="productId",key="#id")
            },
            evict = {
                    @CacheEvict(value = "allProducts", allEntries = true),
                    @CacheEvict(value = "findProductByNames", key = "#product.name",allEntries = true),
                    @CacheEvict(value = "productsStartWith",allEntries = true)

            }
    )
    public Product updateProduct(int id, Product productDetails) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFound("Product With "+id+" Not Found"));

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setStock(productDetails.getStock());

        productRepository.save(product);
        return product;
    }

    @Transactional
    @CachePut(value = "productsPrice", key = "#id")
    public double updateProductPrice(int id,double newProductPrice){
        Product product=productRepository.findById(id).orElseThrow(()->new ProductNotFound("Product "+id+"Not Found"));

        product.setPrice(newProductPrice);
        productRepository.save(product);
        return newProductPrice;
    }


    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "products", key = "#id"),
                    @CacheEvict(value = "productId", key = "#id"),
                    @CacheEvict(value = "productsPrice", key = "#id"),
                    @CacheEvict(value = "allProducts", allEntries = true),
                    @CacheEvict(value = "findProductByNames", key = "#product.name",allEntries = true),
                    @CacheEvict(value = "productsStartWith",allEntries = true)

            }
    )
    public void deleteProduct(int id,int productId){
            Product product=getProductById(productId);
            productRepository.delete(product);
    }


    @Transactional(readOnly = true)
    @Cacheable(value = "productId",key="#id")
    public Product getProductById(int id) throws ProductNotFound {
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFound("Product "+id+"Not Found"));
    }


    @Transactional(readOnly = true)
    @Cacheable(value="productsPrice",key="#id")
    public double findPriceById(int id){
        Optional<Product> product=productRepository.findPriceById(id);
        if(product.isPresent()){
            return product.get().getPrice();
        }
        else{
            throw new ProductNotFound("Product "+id+" Not Found");
        }
    }


    @Cacheable(value = "findProductByNames",key="#name")
    public List<Product> findByProductName(String name){
        try {
            return productRepository.findProductByName(name);
        }
        catch (Exception ex){
            throw new ProductNotFound("Product "+name+"Not Found");
        }
    }

    @Cacheable(value = "allProducts",key="#userId")
    public List<Product> viewProducts(int userId){
         CompletableFuture<List<Product>> completableFutureView=CompletableFuture.supplyAsync(()->{
            double currencyAmount=calculateBaseCurrency(userId);
            return productRepository.findAll().stream().peek(p->p.setPrice(p.getPrice() * currencyAmount)).collect(Collectors.toList());
        });
         return completableFutureView.join();
    }

    public double calculateBaseCurrency(int userId){
        User user=loginService.getUserById(userId);

        Currency currencyConversionCode = countryService.changeCountryToCurrencyCode(user.getCountry());
        double currencyConversionAmount = currencyService.changeToBaseCurrency(currencyConversionCode.toString().toLowerCase());

        return currencyConversionAmount;
    }
}
