package com.ml.challenge.ipmetrics.services;

import com.ml.challenge.ipmetrics.clients.country.CountryInfoDTO;
import com.ml.challenge.ipmetrics.clients.country.Currency;
import com.ml.challenge.ipmetrics.clients.country.Language;
import com.ml.challenge.ipmetrics.clients.currency.CurrencyInfoDTO;
import com.ml.challenge.ipmetrics.clients.locationip.IpLocationDTO;
import com.ml.challenge.ipmetrics.dtos.IpInfoDTO;
import com.ml.challenge.ipmetrics.dtos.IpMetricDto;
import com.ml.challenge.ipmetrics.dtos.IpMetricsResult;
import com.ml.challenge.ipmetrics.exception.IpContextInfoServiceException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
    public static final String CONVERTION_TEXT_DONE = ". 1";
    private IpInfoCoordinatorService ipInfoCoordinatorService;

    @Mock
    private IpLocationService ipLocationService;

    @Mock
    private CountryInfoService countryInfoService;

    @Mock
    private CurrencyInfoService currencyInfoService;

    private DistanceCalculatorService distanceCalculatorService;

    @Mock
    private DataService dataService;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        IpLocationDTO ipLocationDto = getIpLocationDTO();
        when(ipLocationService.getIpLocation(IP)).thenReturn(ipLocationDto);
        CountryInfoDTO countryInfoDto = getCountryInfoDto();
        when(countryInfoService.getCountryInfoByIsoCode(AUS)).thenReturn(countryInfoDto);
        CurrencyInfoDTO currencyInfoDto = getCurrencyInfo();
        when(currencyInfoService.getCurrencyInfo(AUD, USD)).thenReturn(Optional.of(currencyInfoDto));
        doNothing().when(dataService).saveIpInfo(any(IpInfoDTO.class));
        distanceCalculatorService = new DistanceCalculatorService();
        ipInfoCoordinatorService = new IpInfoCoordinatorService(ipLocationService, countryInfoService,
                currencyInfoService, distanceCalculatorService, dataService);
    }

    @Test
    public void shouldReturnIpContextInfo() {
        IpInfoDTO result = ipInfoCoordinatorService.getExternalResult(IP);
        assertNotNull(result);
        assertEquals(IP, result.getIp());
        assertEquals("AUD. 1 AUD = 0.8 USD", result.getCurrency());
        assertEquals(AUS, result.getIsoCode());
        assertEquals(AUSTRALIA, result.getCountry());
        assertEquals(Double.valueOf(9033.799916394963D), result.getDistance());
        assertEquals(ENGLISH, result.getLanguages().get(0));
        assertNotNull(result.getCurrentDate());
        assertEquals("UTC+8", result.getTime().get(0).getUtc());
        assertEquals("UTC+9", result.getTime().get(1).getUtc());
    }


    @Test
    public void shouldThrowsException() {
        when(ipLocationService.getIpLocation("1.1.1.2")).thenReturn(new IpLocationDTO());
        assertThrows(IpContextInfoServiceException.class, () -> {
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

    @Test
    public void shouldReturnResultWithoutDistanceInfo() {
        IpLocationDTO ipLocation = new IpLocationDTO();
        ipLocation.setCountryCode3("UNO");
        ipLocation.setCountryName("UNO");
        when(ipLocationService.getIpLocation(IP2)).thenReturn(ipLocation);
        CountryInfoDTO countryInfoDTO = getCountryInfoDto();
        countryInfoDTO.setLatlng(null);
        when(countryInfoService.getCountryInfoByIsoCode("UNO")).thenReturn(countryInfoDTO);
        IpInfoDTO result = ipInfoCoordinatorService.getExternalResult(IP2);
        assertNotNull(result);
        assertEquals("UNO", result.getCountry());
        assertNotNull(result.getCurrency());
        assertNull(result.getDistance());
    }

    @Test
    public void shouldReturnResultWithoutIncompleteCurrencyInfo() {
        IpLocationDTO ipLocation = new IpLocationDTO();
        ipLocation.setCountryCode3("UNO");
        ipLocation.setCountryName("UNO");
        when(ipLocationService.getIpLocation(IP2)).thenReturn(ipLocation);
        CountryInfoDTO countryInfo = getCountryInfoDto();
        when(countryInfoService.getCountryInfoByIsoCode("UNO")).thenReturn(countryInfo);
        when(currencyInfoService.getCurrencyInfo(countryInfo.getCurrencies().get(0).getCode(), "USD")).thenReturn(Optional.empty());
        IpInfoDTO result = ipInfoCoordinatorService.getExternalResult(IP2);
        assertNotNull(result);
        assertEquals("UNO", result.getCountry());
        assertEquals("AUD", result.getCurrency());
        assertFalse(result.getCurrency().contains(CONVERTION_TEXT_DONE));
    }

    @Test
    public void getMetrics() {
        when(dataService.getMetrics(anyLong())).thenReturn(getIpMetricsResult());
        IpMetricsResult ipMetricsResult = ipInfoCoordinatorService.getMetrics();
        assertEquals(ipMetricsResult.getAverageDistance(), "12473.500862275616");
        assertEquals(3, ipMetricsResult.getIpMetricDtoList().size());
        assertEquals(1L, (long) ipMetricsResult.getId());
        assertNotNull(ipMetricsResult.getIpMetricDtoList().get(0).toString());
    }

    @Test
    public void getEmptyMetrics() {
        IpMetricsResult ipMetricsResultMock = new IpMetricsResult();
        ipMetricsResultMock.setId(1L);
        ipMetricsResultMock.setIpMetricDtoList(new ArrayList<>());
        when(dataService.getMetrics(anyLong())).thenReturn(ipMetricsResultMock);
        IpMetricsResult ipMetricsResult = ipInfoCoordinatorService.getMetrics();
        assertNull(ipMetricsResult.getAverageDistance());
        assertTrue(ipMetricsResult.getIpMetricDtoList().isEmpty());
        assertEquals(1L, (long) ipMetricsResult.getId());
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

    private IpMetricsResult getIpMetricsResult(){
        IpMetricsResult ipMetricsResult = new IpMetricsResult();
        ipMetricsResult.setIpMetricDtoList(getIpMetrics());
        ipMetricsResult.setAverageDistance("12473.500862275616");
        ipMetricsResult.setId(1L);
        return ipMetricsResult;

    }

    private List<IpMetricDto> getIpMetrics(){
        return Arrays.asList(IpMetricDto.builder()
                        .country("COLOMBIA")
                        .distance(23500.20)
                        .invocations(20L).build(),
                IpMetricDto.builder()
                        .country("Australia")
                        .distance(13061.08525474750)
                        .invocations(5L).build(),
                IpMetricDto.builder()
                        .country("Brazil")
                        .distance(2757.3310919779183)
                        .invocations(23L).build());
    }
}
