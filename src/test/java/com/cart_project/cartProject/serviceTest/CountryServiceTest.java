package com.cart_project.cartProject.serviceTest;

import com.cart_project.cartProject.service.CountryService;
import com.neovisionaries.i18n.CountryCode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Currency;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CountryServiceTest {
    private static List<CountryCode> countryCodeList;

    @InjectMocks
    CountryService countryService;
    @BeforeAll
    public static void beforeAll(){
        countryCodeList= CountryCode.findByName("Japan");
    }
    @AfterAll
    public static void afterAll(){
        countryCodeList=null;
    }
    @Test
    public void changeCountryToCurrencyCode(){
        Currency currency=countryService.changeCountryToCurrencyCode("Japan");
        Assertions.assertEquals(countryCodeList.get(0).getCurrency(),currency);
    }
}
