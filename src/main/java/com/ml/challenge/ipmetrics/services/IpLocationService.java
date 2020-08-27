package com.ml.challenge.ipmetrics.services;

import com.ml.challenge.ipmetrics.clients.locationip.IpLocationApiClient;
import com.ml.challenge.ipmetrics.clients.locationip.IpLocationDTO;
import com.ml.challenge.ipmetrics.exception.IpContextInfoServiceException;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static com.ml.challenge.ipmetrics.services.IpInfoCoordinatorService.NO_INFO_FOR_IP;

@Service
@AllArgsConstructor
public class IpLocationService {

    private final IpLocationApiClient ipLocationApiClient;

    @Cacheable(value = {"ipLocation-cache"}, key = "#ip", unless = "#result == null")
    public IpLocationDTO getIpLocation(String ip) {
        return ipLocationApiClient.getIpLocation(ip)
                .orElseThrow(() -> new IpContextInfoServiceException(String.format(NO_INFO_FOR_IP, ip)));
    }

}
