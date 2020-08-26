package com.ml.challenge.ipmetrics.services;

import com.ml.challenge.ipmetrics.clients.currency.CurrencyInfoApiClient;
import com.ml.challenge.ipmetrics.clients.currency.CurrencyInfoDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@AllArgsConstructor
public class CurrencyInfoService {

    public static final String DEFAULT_BASE_CODE = "USD";
    private CurrencyInfoApiClient currencyInfoApiClient;


    public CurrencyInfoDTO getCurrencyInfo(String code, String base) {
        return currencyInfoApiClient.getCurrencyInfo(code, StringUtils.isEmpty(base) ? DEFAULT_BASE_CODE : base);
    }
}
