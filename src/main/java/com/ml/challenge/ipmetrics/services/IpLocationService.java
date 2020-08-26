package com.ml.challenge.ipmetrics.services;

import com.ml.challenge.ipmetrics.clients.locationip.IpLocationApiClient;
import com.ml.challenge.ipmetrics.clients.locationip.IpLocationDTO;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class IpLocationService {

    private final IpLocationApiClient ipLocationApiClient;

    @Cacheable(value = {"ipLocation-cache"}, key = "#ip")
    public IpLocationDTO getIpLocation(String ip) {
        return ipLocationApiClient.getIpLocation(ip);
    }
}
