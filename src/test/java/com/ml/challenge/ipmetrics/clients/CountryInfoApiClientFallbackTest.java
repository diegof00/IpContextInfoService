package com.ml.challenge.ipmetrics.clients;

import com.ml.challenge.ipmetrics.clients.country.CountryInfoApiClientFallback;
import com.ml.challenge.ipmetrics.clients.currency.CurrencyInfoApiClientFallback;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class CountryInfoApiClientFallbackTest {
    private CountryInfoApiClientFallback countryInfoApiClientFallback;


    @Test
    public void shouldReturnEmptyResult() {
        countryInfoApiClientFallback = new CountryInfoApiClientFallback();
        assertFalse(countryInfoApiClientFallback.getCountryInfoByIsoCode("USD").isPresent());
    }
}
