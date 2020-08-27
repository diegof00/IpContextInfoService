package com.ml.challenge.ipmetrics.services;

import com.ml.challenge.ipmetrics.clients.country.CountryInfoApiClient;
import com.ml.challenge.ipmetrics.clients.country.CountryInfoDTO;
import com.ml.challenge.ipmetrics.exception.IpContextInfoServiceException;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CountryInfoService {

    private final CountryInfoApiClient countryInfoApiClient;

    @Cacheable(value = {"country-cache"}, key = "#code", unless = "#result == null")
    public CountryInfoDTO getCountryInfoByIsoCode(String code) {
        return countryInfoApiClient.getCountryInfoByIsoCode(code)
                .orElseThrow(() -> new IpContextInfoServiceException(String.format("No country info for %S", code)));
    }
}
