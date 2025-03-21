package com.cart_project.cartProject.serviceTest;

import com.cart_project.cartProject.entity.User;
import com.cart_project.cartProject.exceptions.ProductNotFound;
import com.cart_project.cartProject.entity.Product;
import com.cart_project.cartProject.repository.ProductRepository;
import com.cart_project.cartProject.service.CountryService;
import com.cart_project.cartProject.service.CurrencyService;
import com.cart_project.cartProject.service.LoginService;
import com.cart_project.cartProject.service.ProductService;
import com.neovisionaries.i18n.CountryCode;
import lombok.extern.java.Log;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {


    private static Product product;
    private static User user;
    private static List<Product> productList;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CountryService countryService;
    @Mock
    private CurrencyService currencyService;
    @Mock
    private LoginService loginService;
    @InjectMocks
    private ProductService productService;

    @BeforeAll
    public static void beforeAll(){
        product=new Product();
        product.setStock(7);
        product.setId(1);
        product.setDescription("Smooth Ball Point Pen");
        product.setPrice(999);
        product.setName("Blue Pen");

        productList= Arrays.asList(new Product(1,"wire","Best One",12,30));
        user=new User(1,"mani","kms@gmail.com","kdwqw","user","India",null);

    }

    @AfterAll
    public static void afterAll(){
        product=null;
    }

    @Test
    public void addProductTest(){

        Mockito.when(productRepository.save(product)).thenReturn(product);
        Product newProduct=productService.addProduct(product);
        Assertions.assertEquals(product.getName(),newProduct.getName());
    }

    @Test
    public void getProductByIdTest(){

        Mockito.when(productRepository.findById(1)).thenReturn(Optional.of(product));
        Product addedProduct=productService.getProductById(1);
        Assertions.assertEquals(1,addedProduct.getId());
        Assertions.assertEquals("Blue Pen",addedProduct.getName());
    }

    @Test
    public void getProductById_ProductDoesNotExist(){
        Mockito.when(productRepository.findById(10)).thenReturn(Optional.empty());
        Assertions.assertThrows(ProductNotFound.class,()->{
            productService.getProductById(10);
        });
    }

    @Test
    public void ViewProductsTest(){
        Mockito.when(productRepository.findAll()).thenReturn(productList);
        Mockito.when(loginService.getUserById(1)).thenReturn(user);
        List<CountryCode> code=CountryCode.findByName("Japan");
        Mockito.when(countryService.changeCountryToCurrencyCode(user.getCountry())).thenReturn(code.get(0).getCurrency());
        Mockito.when(currencyService.changeToBaseCurrency(code.get(0).getCurrency().toString().toLowerCase())).thenReturn(11.0);

        List<Product> products=productService.viewProducts(1);

        Assertions.assertEquals(1,products.size());
        Assertions.assertEquals(132,products.get(0).getPrice());
    }

    @Test
    public void productDeleteTest(){
        Mockito.when(productRepository.findById(1)).thenReturn(Optional.of(product));
        Mockito.doNothing().when(productRepository).delete(Mockito.any(Product.class));
        productService.deleteProduct(1, 1);
        Mockito.verify(productRepository).delete(Mockito.any(Product.class));
    }

    @Test
    public void findPriceByIdTest(){
        Mockito.when(productRepository.findPriceById(1)).thenReturn(Optional.of(product));
        double price1=productService.findPriceById(1);
        Assertions.assertEquals(77,price1);
        Assertions.assertThrows(ProductNotFound.class,()-> productService.findPriceById(77)
        );

    }

    @Test
    public void updateProductTest(){
        Mockito.when(productRepository.findById(1)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(product)).thenReturn(product);
        productService.updateProduct(1,new Product(1,"Black Pen","Best One",12,30));
        Assertions.assertEquals("Black Pen",product.getName());
        Assertions.assertThrows(ProductNotFound.class,()->{
            productService.updateProduct(2,new Product(1,"Black Pen","Best One",12,30));
        });
    }

    @Test
    public void updateProductPriceTest(){
        Mockito.when(productRepository.findById(1)).thenReturn(Optional.ofNullable(product));
        Mockito.when(productRepository.save(product)).thenReturn(product);
        productService.updateProductPrice(1,77);
        Assertions.assertEquals(77,product.getPrice());

    }


}
