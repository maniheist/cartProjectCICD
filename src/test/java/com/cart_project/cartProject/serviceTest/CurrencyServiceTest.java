package com.cart_project.cartProject.serviceTest;

import com.cart_project.cartProject.service.CurrencyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class CurrencyServiceTest {
    @InjectMocks
    CurrencyService currencyService;
    @Mock
    RestTemplate restTemplate;


    @Test
    public void changeToBaseCurrencyTest(){
        String mockJson="{ \"eur\": { \"usd\": 1.08, \"inr\": 90.5 } }";
        double convertedCurrency=currencyService.changeToBaseCurrency("usd");
        Assertions.assertEquals(convertedCurrency,convertedCurrency);

    }
}
