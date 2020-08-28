package com.ml.challenge.ipmetrics.services;

import com.ml.challenge.ipmetrics.dtos.IpInfoDTO;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class IpInfoCoordinatorServiceTest {

    private static final String IP = "5.5.5.5";
    private IpInfoCoordinatorService ipInfoCoordinatorService;

    @Mock
    private IpLocationService ipLocationService;

    @Mock
    private CountryInfoService countryInfoService;

    @Mock
    private CurrencyInfoService currencyInfoService;

    @Mock
    private DistanceCalculatorService distanceCalculatorService;

    @Mock
    private DataStoreService dataStoreService;


    @Before
    public void setup() {
        ipInfoCoordinatorService = new IpInfoCoordinatorService(ipLocationService, countryInfoService,
                currencyInfoService, distanceCalculatorService, dataStoreService);
    }


    @Test
    public void test() {
        IpInfoDTO result = ipInfoCoordinatorService.getExternalResult(IP);


    }

}
