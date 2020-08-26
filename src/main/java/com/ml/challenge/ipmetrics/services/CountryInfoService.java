package com.ml.challenge.ipmetrics.services;

import com.ml.challenge.ipmetrics.clients.country.CountryInfoApiClient;
import com.ml.challenge.ipmetrics.clients.country.CountryInfoDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CountryInfoService {

    private CountryInfoApiClient countryInfoApiClient;

    public CountryInfoDTO getCountryInfoByIsoCode(String code) {
        return countryInfoApiClient.getCountryInfoByIsoCode(code);
    }
}
