package com.ml.challenge.ipmetrics.clients;

import com.ml.challenge.ipmetrics.clients.locationip.IpLocationApiClient;
import com.ml.challenge.ipmetrics.clients.locationip.IpLocationDTO;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class IpLocationApiClientTest {

    @Test
    public void shouldReturnIpLocation() {
        IpLocationApiClient ipLocationApiClient = new IpLocationApiClient() {
            @Override
            public Optional<IpLocationDTO> getIpLocation(String ip) {
                IpLocationDTO ipLocation = new IpLocationDTO();
                ipLocation.setCountryName("CountryName");
                ipLocation.setCountryCode3("COD");
                ipLocation.setCountryCode("CODE");
                return Optional.of(ipLocation);
            }
        };

        Optional<IpLocationDTO> result = ipLocationApiClient.getIpLocation("IP");
        assertTrue(result.isPresent());
    }

}
