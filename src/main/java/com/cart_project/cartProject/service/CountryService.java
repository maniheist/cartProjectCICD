package com.cart_project.cartProject.service;

import com.neovisionaries.i18n.CountryCode;
import org.springframework.stereotype.Service;

import java.util.Currency;
import java.util.List;
@Service
public class CountryService {
    public Currency changeCountryToCurrencyCode(String countryName){
        List<CountryCode> country=CountryCode.findByName(countryName);
        Currency currency=country.get(0).getCurrency();
        return currency;
    }
}
