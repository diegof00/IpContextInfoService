package com.ml.challenge.ipmetrics.clients.currency;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="${external.service.currencyinfo.name}", url="${external.service.currencyinfo.url}")
public interface CurrencyInfoApiClient {

    @GetMapping(value = "/latest", produces = "application/json")
    CurrencyInfoDTO getCurrencyInfo(@RequestParam("symbol") String symbol, @RequestParam("base") String base);

}
