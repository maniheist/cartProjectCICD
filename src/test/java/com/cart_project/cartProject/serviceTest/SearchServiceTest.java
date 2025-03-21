package com.cart_project.cartProject.serviceTest;

import com.cart_project.cartProject.entity.Product;
import com.cart_project.cartProject.entity.User;
import com.cart_project.cartProject.repository.ProductRepository;
import com.cart_project.cartProject.service.*;
import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class SearchServiceTest {

    private static Product product;
    private static List<Product> productList;
    private static Page<Product> productsPageAsc;
    private static Page<Product> productsPageDesc;
    private static int page=0;
    private static int size=2;
    private static Pageable pageableAsc;
    private static Pageable pageableDesc;
    private static User user;
    @Mock
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private LoginService loginService;
    @Mock
    private CountryService countryService;
    @Mock
    private CurrencyService currencyService;
    @InjectMocks
    private SearchService searchService;

    @BeforeAll
    public static void beforeAll(){
        product=new Product();
        product.setStock(7);
        product.setId(10);
        product.setDescription("Smooth Ball Point Pen");
        product.setPrice(999);
        product.setName("Blue Pen");
        productList= Arrays.asList(new Product(1,"wire","Best One",12,30));
        pageableAsc= PageRequest.of(page,size, Sort.by("price").ascending());
        productsPageAsc = new PageImpl<>(productList, pageableAsc, productList.size());
        pageableDesc= PageRequest.of(page,size, Sort.by("price").descending());
        productsPageDesc = new PageImpl<>(productList, pageableDesc, productList.size());
        user=new User();
        user.setId(1);
        user.setName("randy");
        user.setPassword("randy@11");
        user.setEmail("randy2611@gmail.com");
        user.setRole("User");
        user.setCountry("Japan");
    }
    @Test
    public void productWithInPriceRangeTest(){
        List<CountryCode> code=CountryCode.findByName(user.getCountry());
        Mockito.when(loginService.getUserById(1)).thenReturn(user);
        Mockito.when(productService.findByProductName("Blue Pen")).thenReturn(productList);
        Mockito.when(countryService.changeCountryToCurrencyCode(user.getCountry())).thenReturn(code.get(0).getCurrency());
        Mockito.when(currencyService.changeToBaseCurrency(code.get(0).getCurrency().toString().toLowerCase())).thenReturn(10.0);

        List<Product> products=searchService.productWithInPriceRange(1,"Blue Pen",10,200000);
        Assertions.assertEquals(1,products.size());

    }

    @Test
    public void allProductsThatStartWithTest(){
        List<CountryCode> code=CountryCode.findByName(user.getCountry());
        Mockito.when(productService.viewProducts(1)).thenReturn(productList);
        List<Product> products=searchService.allProductsThatStartWith(1,"w");

        Assertions.assertEquals("wire",products.get(0).getName());
        Assertions.assertEquals(12.0,products.get(0).getPrice());
    }

    @Test
    public void leastPriceProductsFilteringTest(){
        Mockito.when(productRepository.findByNameContainingIgnoreCase(pageableAsc,product.getName())).thenReturn(productsPageAsc);
        Page<Product> productPageResult=searchService.leastPriceProductsFiltering(product.getName(),page,size);
        Assertions.assertEquals(productsPageAsc.getSize(),productPageResult.getSize());

    }

    @Test
    public void highPriceProductsFilteringTest(){
        Mockito.when(productRepository.findByNameContainingIgnoreCase(pageableDesc,product.getName())).thenReturn(productsPageDesc);
        Page<Product> productPageResult=searchService.highPricesProductsFiltering(product.getName(),page,size);
        Assertions.assertEquals(productsPageDesc.getSize(),productPageResult.getSize());

    }

}
