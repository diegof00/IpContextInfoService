package com.ml.challenge.ipmetrics.clients.currency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CurrencyInfoServiceFallback implements CurrencyInfoService {

    private static final Logger log = LoggerFactory.getLogger(CurrencyInfoServiceFallback.class);

    @Override
    public CurrencyInfoDTO getCurrencyInfo(String symbol, String base) {
        log.info("error calling currency info for currency symbol: {}", symbol);
        return new CurrencyInfoDTO();
    }
}
