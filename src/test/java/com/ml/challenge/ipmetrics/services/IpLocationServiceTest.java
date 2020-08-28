package com.ml.challenge.ipmetrics.services;

import com.ml.challenge.ipmetrics.clients.locationip.IpLocationApiClient;
import com.ml.challenge.ipmetrics.clients.locationip.IpLocationDTO;
import com.ml.challenge.ipmetrics.exception.IpContextInfoServiceException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

public class IpLocationServiceTest {

    private static final String IP = "8.5.6.4";
    public static final String UNITED_STATES = "United States";
    public static final String US = "US";
    public static final String USA = "USA";
    private IpLocationService ipLocationService;

    @Mock
    private IpLocationApiClient ipLocationApiClient;

    private IpLocationDTO ipLocationDTO;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ipLocationService = new IpLocationService(ipLocationApiClient);
        ipLocationDTO = new IpLocationDTO();
        ipLocationDTO.setCountryCode(US);
        ipLocationDTO.setCountryCode3(USA);
        ipLocationDTO.setCountryName(UNITED_STATES);
    }

    @Test
    public void shouldReturnIpLocation() {
        when(ipLocationApiClient.getIpLocation(IP)).thenReturn(Optional.of(ipLocationDTO));
        IpLocationDTO result = ipLocationService.getIpLocation(IP);
        assertNotNull(result);
        assertEquals(UNITED_STATES, result.getCountryName());
        assertEquals(US, result.getCountryCode());
        assertEquals(USA, result.getCountryCode3());
    }

    @Test
    public void shouldThrowsExceptionWhenIpLocationInfoNotExists() {
        assertThrows(IpContextInfoServiceException.class, ()->{
            ipLocationService.getIpLocation("1.1.1.1");
        });
    }

}
