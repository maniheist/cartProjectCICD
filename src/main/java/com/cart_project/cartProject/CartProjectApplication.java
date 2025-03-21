package com.cart_project.cartProject;

import com.cart_project.cartProject.entity.Product;
import com.cart_project.cartProject.repository.*;
import com.neovisionaries.i18n.CountryCode;
import com.neovisionaries.i18n.CurrencyCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Currency;
import java.util.List;

@RequiredArgsConstructor
@SpringBootApplication
@EnableCaching
public class CartProjectApplication {

	public static void main(String[] args) {
	ConfigurableApplicationContext context= SpringApplication.run(CartProjectApplication.class, args);
//	ProductRepository productRepository=context.getBean(ProductRepository.class);

//	productRepository.save(Product.builder().id(3).name("Glass").price(101).stock(29).build());

//		loginRepository.save(new User(7,"Mani","kmsreddy2611@gmail.com","Mani@111","user"));
//		CartRepository cartRepository=context.getBean(CartRepository.class);
//		cartRepository.save(new Cart(1,7,1,1));
//		List<CountryCode> country=CountryCode.findByName("Japan");
//		System.out.println(country);
//		CountryCode[] codes=CountryCode.values();
//		for (CountryCode code : codes) {
//			System.out.println("Country: " + code.getName()+ ", Currency: " + code.getCurrency());
//		}
//		Currency currency=country.get(0).getCurrency();
//		System.out.println(currency);


	}
}
