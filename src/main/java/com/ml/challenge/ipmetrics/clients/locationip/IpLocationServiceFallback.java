package com.ml.challenge.ipmetrics.clients.locationip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class IpLocationServiceFallback implements IpLocationService {

    private static final Logger log = LoggerFactory.getLogger(IpLocationServiceFallback.class);

    @Override
    public IpLocationDTO getIpLocation(String ip) {
        log.info("error calling IpLocation info for ip: {}", ip);
        return new IpLocationDTO();
    }
}
