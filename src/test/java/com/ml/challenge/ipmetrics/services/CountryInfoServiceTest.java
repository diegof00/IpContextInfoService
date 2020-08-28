package com.ml.challenge.ipmetrics.services;

import com.ml.challenge.ipmetrics.clients.country.CountryInfoApiClient;
import com.ml.challenge.ipmetrics.clients.country.CountryInfoDTO;
import com.ml.challenge.ipmetrics.clients.country.Currency;
import com.ml.challenge.ipmetrics.exception.IpContextInfoServiceException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

public class CountryInfoServiceTest {

    public static final String COP = "COP";
    public static final String PESO = "PESO";
    public static final String SYMBOL = "$";
    public static final String CODE = "CO";
    public static final String COUNTRY = "Colombia";

    @Mock
    private CountryInfoApiClient countryInfoApiClient;

    private CountryInfoService countryInfoService;

    private CountryInfoDTO countryInfoDTO;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        countryInfoService = new CountryInfoService(countryInfoApiClient);
        countryInfoDTO = new CountryInfoDTO();
        countryInfoDTO.setCode(CODE);
        countryInfoDTO.setName(COUNTRY);
        Currency currency = new Currency();
        currency.setCode(COP);
        currency.setName(PESO);
        currency.setSymbol(SYMBOL);
        countryInfoDTO.setCurrencies(Collections.singletonList(currency));
    }

    @Test
    public void shouldReturnCountryInfoByIsoCode() {
        when(countryInfoApiClient.getCountryInfoByIsoCode(CODE)).thenReturn(Optional.of(countryInfoDTO));
        CountryInfoDTO result = countryInfoService.getCountryInfoByIsoCode(CODE);
        assertNotNull(result);
        assertEquals(CODE, result.getCode());
        assertEquals(COUNTRY, result.getName());
        assertNotNull(result.getCurrencies());
        assertFalse(result.getCurrencies().isEmpty());
        assertEquals(COP, result.getCurrencies().get(0).getCode());
        assertEquals(PESO, result.getCurrencies().get(0).getName());
        assertEquals(SYMBOL, result.getCurrencies().get(0).getSymbol());
    }

    @Test
    public void shouldThrowsExceptionWhenThereIsNoCountryInfoByIsoCode() {
        assertThrows(IpContextInfoServiceException.class, () -> {
            countryInfoService.getCountryInfoByIsoCode(CODE);
        });
    }

}
