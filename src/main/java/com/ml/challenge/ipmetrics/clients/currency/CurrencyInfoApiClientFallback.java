package com.ml.challenge.ipmetrics.clients.currency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CurrencyInfoApiClientFallback implements CurrencyInfoApiClient {

    private static final Logger log = LoggerFactory.getLogger(CurrencyInfoApiClientFallback.class);

    @Override
    public Optional<CurrencyInfoDTO> getCurrencyInfo(String symbol, String base) {
        log.info("error calling currency info for currency symbol: {}", symbol);
        return Optional.empty();
    }
}
