package com.cart_project.cartProject.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CaffeineCacheManager cacheManager(){
        CaffeineCacheManager caffeineCacheManager=new CaffeineCacheManager(
                "products", "productId", "productsPrice", "findProductByNames", "allProducts","productsStartWith"
        );
        caffeineCacheManager.setCaffeine(Caffeine.newBuilder().maximumSize(500).expireAfterWrite(10, TimeUnit.MINUTES).recordStats());

        return caffeineCacheManager;
    }
}
