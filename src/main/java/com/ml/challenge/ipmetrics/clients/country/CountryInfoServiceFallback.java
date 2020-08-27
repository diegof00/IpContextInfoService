package com.ml.challenge.ipmetrics.clients.country;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CountryInfoServiceFallback implements CountryInfoService {

    private static final Logger log = LoggerFactory.getLogger(CountryInfoServiceFallback.class);

    @Override
    public CountryInfoDTO getCountryInfoByIsoCode(String code) {
        log.info("error calling countryInfo service for iso code: {}", code);
        return new CountryInfoDTO();
    }
}
