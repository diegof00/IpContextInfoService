package com.ml.challenge.ipmetrics.clients.country;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="${external.service.countryinfo.name}", url="${external.service.countryinfo.url}", fallback = CountryInfoServiceFallback.class)
public interface CountryInfoService {

    @GetMapping(value = "/alpha/{code}", produces = "application/json")
    CountryInfoDTO getCountryInfoByIsoCode(@PathVariable String code);

}
