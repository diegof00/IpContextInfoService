package com.ml.challenge.ipmetrics.services;

import com.ml.challenge.ipmetrics.clients.country.CountryInfoDTO;
import com.ml.challenge.ipmetrics.clients.country.Currency;
import com.ml.challenge.ipmetrics.clients.country.Language;
import com.ml.challenge.ipmetrics.clients.currency.CurrencyInfoDTO;
import com.ml.challenge.ipmetrics.clients.locationip.IpLocationDTO;
import com.ml.challenge.ipmetrics.dtos.CountryTimeZone;
import com.ml.challenge.ipmetrics.dtos.IpInfoDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class IpInfoCoordinatorService {

    private final IpLocationService ipLocationService;
    private final CountryInfoService countryInfoService;
    private final CurrencyInfoService currencyInfoService;

    public IpInfoDTO getExternalResult(String ip) {
        IpLocationDTO ipLocationDTO = ipLocationService.getIpLocation(ip);
        IpInfoDTO ipInfoDTO = new IpInfoDTO();
        ipInfoDTO.setIp(ip);
        ipInfoDTO.setCountry(ipLocationDTO.getCountryName());
        ipInfoDTO.setIsoCode(ipLocationDTO.getCountryCode3());
        CountryInfoDTO countryInfoDTO = countryInfoService.getCountryInfoByIsoCode(ipInfoDTO.getIsoCode());
        ipInfoDTO.setLanguages(countryInfoDTO.getLanguages().stream().map(Language::getName).collect(Collectors.toList()));
        ipInfoDTO.setTime(resolveCountryTimeZone(countryInfoDTO.getTimezones()));
        ZonedDateTime date = ipInfoDTO.getTime().stream().findFirst().map(CountryTimeZone::getTime).orElseGet(ZonedDateTime::now);
        ipInfoDTO.setCurrentDate(date.toLocalDate());
        CurrencyInfoDTO currencyDTO = currencyInfoService.getCurrencyInfo(countryInfoDTO.getCurrencies().get(0).getCode(), "USD");
        ipInfoDTO.setCurrency(resolveCurrency(countryInfoDTO, currencyDTO));
        return ipInfoDTO;
    }

    private List<CountryTimeZone> resolveCountryTimeZone(String[] timeZones) {
        return Stream.of(timeZones).map(utc -> new CountryTimeZone(utc, ZonedDateTime.now(ZoneId.of(utc))))
                .collect(Collectors.toList());
    }

    private String resolveCurrency(CountryInfoDTO countryInfoDTO, CurrencyInfoDTO currencyDTO) {
        String code = countryInfoDTO.getCurrencies().stream().findFirst().map(Currency::getCode).orElse("");
        return code + ". 1 " + code + " = " + currencyDTO.getRates().get(code) + " " + currencyDTO.getBase();
    }
}
