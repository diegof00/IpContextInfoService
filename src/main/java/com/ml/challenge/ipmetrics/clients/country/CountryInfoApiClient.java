package com.ml.challenge.ipmetrics.clients.country;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name="${external.service.countryinfo.name}", url="${external.service.countryinfo.url}", fallback = CountryInfoApiClientFallback.class)
public interface CountryInfoApiClient {

    @GetMapping(value = "/alpha/{code}", produces = "application/json")
    Optional<CountryInfoDTO> getCountryInfoByIsoCode(@PathVariable String code);

}
