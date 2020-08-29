package com.ml.challenge.ipmetrics.services;

import com.ml.challenge.ipmetrics.clients.country.CountryInfoDTO;
import com.ml.challenge.ipmetrics.clients.country.Currency;
import com.ml.challenge.ipmetrics.clients.country.Language;
import com.ml.challenge.ipmetrics.clients.currency.CurrencyInfoDTO;
import com.ml.challenge.ipmetrics.clients.locationip.IpLocationDTO;
import com.ml.challenge.ipmetrics.dtos.IpInfoDTO;
import com.ml.challenge.ipmetrics.exception.IpContextInfoServiceException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

public class IpInfoCoordinatorServiceTest {

    public static final String USD = "USD";
    public static final String AUD = "AUD";
    public static final String AUS = "AUS";
    public static final String IP = "1.1.1.1";
    public static final String AUSTRALIA = "AUSTRALIA";
    public static final String AUSTRALIAN_DOLLAR = "australian dollar";
    public static final String AUD_$ = "AUD$";
    public static final String ENGLISH = "English";
    public static final String IP2 = "1.1.1.2";
    private IpInfoCoordinatorService ipInfoCoordinatorService;

    @Mock
    private IpLocationService ipLocationService;

    @Mock
    private CountryInfoService countryInfoService;

    @Mock
    private CurrencyInfoService currencyInfoService;

    private DistanceCalculatorService distanceCalculatorService;

    @Mock
    private DataStoreService dataStoreService;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        IpLocationDTO ipLocationDto = getIpLocationDTO();
        when(ipLocationService.getIpLocation(IP)).thenReturn(ipLocationDto);
        CountryInfoDTO countryInfoDto = getCountryInfoDto();
        when(countryInfoService.getCountryInfoByIsoCode(AUS)).thenReturn(countryInfoDto);
        CurrencyInfoDTO currencyInfoDto = getCurrencyInfo();
        when(currencyInfoService.getCurrencyInfo(AUD, USD)).thenReturn(currencyInfoDto);
        doNothing().when(dataStoreService).saveDto(any(IpInfoDTO.class));
        distanceCalculatorService = new DistanceCalculatorService();
        ipInfoCoordinatorService = new IpInfoCoordinatorService(ipLocationService, countryInfoService,
                currencyInfoService, distanceCalculatorService, dataStoreService);
    }

    @Test
    public void shouldReturnIpContextInfo() {
        IpInfoDTO result = ipInfoCoordinatorService.getExternalResult(IP);
        assertNotNull(result);
        assertEquals(IP, result.getIp());
        assertEquals("AUD. 1 AUD = 0.8 USD", result.getCurrency());
        assertEquals(AUS, result.getIsoCode());
        assertEquals(AUSTRALIA, result.getCountry());
        assertEquals("9033.799916394963 KM", result.getDistance());
        assertEquals(ENGLISH, result.getLanguages().get(0));
        assertEquals(LocalDateTime.now().plusHours(8).toLocalDate(), result.getCurrentDate());
        assertEquals("UTC+8", result.getTime().get(0).getUtc());
        assertEquals("UTC+9", result.getTime().get(1).getUtc());
    }


    @Test
    public void shouldThrowsException() {
        when(ipLocationService.getIpLocation("1.1.1.2")).thenReturn(new IpLocationDTO());
        assertThrows(IpContextInfoServiceException.class, ()->{
            ipInfoCoordinatorService.getExternalResult("1.1.1.2");
        });
    }

    @Test
    public void shouldReturnResultWithoutCurrencyInfo() {
        IpLocationDTO ipLocation = new IpLocationDTO();
        ipLocation.setCountryCode3("UNO");
        ipLocation.setCountryName("UNO");
        when(ipLocationService.getIpLocation(IP2)).thenReturn(ipLocation);
        when(countryInfoService.getCountryInfoByIsoCode("UNO")).thenReturn(new CountryInfoDTO());
        IpInfoDTO result = ipInfoCoordinatorService.getExternalResult(IP2);
        assertNotNull(result);
        assertEquals("UNO", result.getCountry());
        assertNull("UNO", result.getCurrency());
    }



    private CountryInfoDTO getCountryInfoDto() {
        CountryInfoDTO countryInfoDTO = new CountryInfoDTO();
        countryInfoDTO.setCode(AUS);
        countryInfoDTO.setName(AUSTRALIA);
        countryInfoDTO.setLatlng(new String[]{"12", "12"});
        countryInfoDTO.setTimezones(new String[]{"UTC+8", "UTC+9"});
        Language language = new Language();
        language.setName(ENGLISH);
        language.setNativeName(ENGLISH);
        countryInfoDTO.setLanguages(Collections.singletonList(language));
        Currency currency = new Currency();
        currency.setName(AUSTRALIAN_DOLLAR);
        currency.setCode(AUD);
        currency.setSymbol(AUD_$);
        countryInfoDTO.setCurrencies(Collections.singletonList(currency));
        return countryInfoDTO;
    }

    private IpLocationDTO getIpLocationDTO() {
        IpLocationDTO ipLocationDto = new IpLocationDTO();
        ipLocationDto.setCountryName(AUSTRALIA);
        ipLocationDto.setCountryCode("AU");
        ipLocationDto.setCountryCode3(AUS);
        return ipLocationDto;
    }

    private CurrencyInfoDTO getCurrencyInfo() {
        CurrencyInfoDTO currencyInfoDTO = new CurrencyInfoDTO();
        currencyInfoDTO.setBase(USD);
        Map<String, Float> rates = new HashMap<>();
        rates.put(AUD, 0.80F);
        currencyInfoDTO.setRates(rates);
        currencyInfoDTO.setDate("01/01/2020");
        return currencyInfoDTO;
    }

}
