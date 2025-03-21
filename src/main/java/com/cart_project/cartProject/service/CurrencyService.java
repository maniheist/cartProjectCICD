package com.cart_project.cartProject.service;

import com.cart_project.cartProject.exceptions.CurrencyNotFound;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CurrencyService {

    public double changeToBaseCurrency(String countryCode) {
        String URL = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/eur.json";
        RestTemplate restTemplate = new RestTemplate();

        try {
            String rawJson = restTemplate.getForObject(URL, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(rawJson);
            JsonNode eurNode = rootNode.path("eur");
            if (!eurNode.has(countryCode.toLowerCase())) {
                throw new CurrencyNotFound("Invalid Country Code: " + countryCode);
            }

            double exchangeRate = eurNode.get(countryCode.toLowerCase()).asDouble();
            return exchangeRate;

        } catch (Exception e) {
            throw new CurrencyNotFound("Failed to retrieve currency data");
        }
    }
}

