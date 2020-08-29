package com.ml.challenge.ipmetrics.services;

import com.ml.challenge.ipmetrics.clients.currency.CurrencyInfoApiClient;
import com.ml.challenge.ipmetrics.clients.currency.CurrencyInfoDTO;
import com.ml.challenge.ipmetrics.exception.IpContextInfoServiceException;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

import static com.ml.challenge.ipmetrics.services.IpInfoCoordinatorService.NO_INFO_FOR_IP;

@Service
@AllArgsConstructor
public class CurrencyInfoService {

    public static final String DEFAULT_BASE_CODE = "USD";
    public static final String NO_CURRENCY_INFO_FOR_S = "No currency info for %S";
    private final CurrencyInfoApiClient currencyInfoApiClient;

    @Cacheable(value = {"currency-cache"}, key = "#code", unless = "#result == null")
    public Optional<CurrencyInfoDTO> getCurrencyInfo(String code, String base) {
        return currencyInfoApiClient.getCurrencyInfo(code, StringUtils.isEmpty(base) ? DEFAULT_BASE_CODE : base);
    }
}
