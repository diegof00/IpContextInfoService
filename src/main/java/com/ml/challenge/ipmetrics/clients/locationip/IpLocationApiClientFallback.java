package com.ml.challenge.ipmetrics.clients.locationip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class IpLocationApiClientFallback implements IpLocationApiClient {

    private static final Logger log = LoggerFactory.getLogger(IpLocationApiClientFallback.class);

    @Override
    public Optional<IpLocationDTO> getIpLocation(String ip) {
        log.info("error calling IpLocation info for ip: {}", ip);
        return Optional.empty();
    }
}
