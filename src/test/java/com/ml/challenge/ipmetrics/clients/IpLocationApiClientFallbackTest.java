package com.ml.challenge.ipmetrics.clients;

import com.ml.challenge.ipmetrics.clients.locationip.IpLocationApiClientFallback;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class IpLocationApiClientFallbackTest {

    private IpLocationApiClientFallback ipLocationApiClientFallback;


    @Test
    public void shouldReturnEmptyResult() {
        ipLocationApiClientFallback = new IpLocationApiClientFallback();
        assertFalse(ipLocationApiClientFallback.getIpLocation("IP").isPresent());
    }

}
