package com.ml.challenge.ipmetrics.clients.country;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CountryInfoApiClientFallback implements CountryInfoApiClient {

    private static final Logger log = LoggerFactory.getLogger(CountryInfoApiClientFallback.class);

    @Override
    public Optional<CountryInfoDTO > getCountryInfoByIsoCode(String code) {
        log.info("error calling countryInfo service for iso code: {}", code);
        return Optional.empty();
    }
}
