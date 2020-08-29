package com.ml.challenge.ipmetrics.clients;

import com.ml.challenge.ipmetrics.clients.currency.CurrencyInfoApiClientFallback;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class CurrencyInfoApiClientFallbackTest {

    private CurrencyInfoApiClientFallback currencyInfoApiClientFallback;


    @Test
    public void shouldReturnEmptyResult() {
        currencyInfoApiClientFallback = new CurrencyInfoApiClientFallback();
        assertFalse(currencyInfoApiClientFallback.getCurrencyInfo("USD", "USD").isPresent());
    }
}
