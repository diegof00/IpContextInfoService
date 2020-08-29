package com.ml.challenge.ipmetrics.clients.currency;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@FeignClient(name="${external.service.currencyinfo.name}", url="${external.service.currencyinfo.url}", fallback = CurrencyInfoApiClientFallback.class)
public interface CurrencyInfoApiClient {

    @GetMapping(value = "/latest", produces = "application/json")
    Optional<CurrencyInfoDTO> getCurrencyInfo(@RequestParam("symbols") String symbol, @RequestParam("base") String base);

}
