package com.ml.challenge.ipmetrics.services;

import com.ml.challenge.ipmetrics.clients.currency.CurrencyInfoApiClient;
import com.ml.challenge.ipmetrics.clients.currency.CurrencyInfoDTO;
import com.ml.challenge.ipmetrics.exception.IpContextInfoServiceException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class CurrencyInfoServiceTest {

    public static final String USD = "USD";
    public static final String GBP = "GBP";

    private CurrencyInfoService currencyInfoService;

    @Mock
    private CurrencyInfoApiClient currencyInfoApiClient;

    private CurrencyInfoDTO currencyInfoDTO;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        currencyInfoService = new CurrencyInfoService(currencyInfoApiClient);
        currencyInfoDTO = new CurrencyInfoDTO();
        currencyInfoDTO.setBase(USD);
        Map<String, Float> rates = new HashMap<>();
        rates.put(GBP, 1.10F);
        currencyInfoDTO.setRates(rates);
        currencyInfoDTO.setDate("01/01/2020");
    }

    @Test
    public void shouldReturnCountryInfoByIsoCode() {
        when(currencyInfoApiClient.getCurrencyInfo(GBP, USD)).thenReturn(Optional.of(currencyInfoDTO));
        Optional<CurrencyInfoDTO> result = currencyInfoService.getCurrencyInfo(GBP, USD);
        assertTrue(result.isPresent());
        assertEquals(USD, result.get().getBase());
        assertNotNull(result.get().getRates());
        assertFalse(result.get().getRates().entrySet().isEmpty());
        assertEquals(1.10F, result.get().getRates().get(GBP), 0F);
        assertEquals("01/01/2020", result.get().getDate());
        assertEquals("01/01/2020", result.get().getDate());
    }

    @Test
    public void shouldReturnCountryInfoWithUSDBaseWhenBaseIsEmpty() {
        when(currencyInfoApiClient.getCurrencyInfo(GBP, USD)).thenReturn(Optional.of(currencyInfoDTO));
        Optional<CurrencyInfoDTO> result = currencyInfoService.getCurrencyInfo(GBP, "");
        assertTrue(result.isPresent());
        assertEquals(USD, result.get().getBase());
        assertNotNull(result.get().getRates());
    }

    @Test
    public void shouldReturnCountryInfoWithUSDBaseWhenBaseIsNull() {
        when(currencyInfoApiClient.getCurrencyInfo(GBP, USD)).thenReturn(Optional.of(currencyInfoDTO));
        Optional<CurrencyInfoDTO> result = currencyInfoService.getCurrencyInfo(GBP, null);
        assertTrue(result.isPresent());
        assertEquals(USD, result.get().getBase());
        assertNotNull(result.get().getRates());
    }

    @Test
    public void shouldReturnEmptyOptionalWhenThereIsNoInfoCode() {
        assertFalse(currencyInfoService.getCurrencyInfo(GBP, USD).isPresent());
    }

}
