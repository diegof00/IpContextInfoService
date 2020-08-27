package com.ml.challenge.ipmetrics.services;

import com.ml.challenge.ipmetrics.clients.country.CountryInfoDTO;
import com.ml.challenge.ipmetrics.clients.country.CountryInfoService;
import com.ml.challenge.ipmetrics.clients.country.Currency;
import com.ml.challenge.ipmetrics.clients.country.Language;
import com.ml.challenge.ipmetrics.clients.currency.CurrencyInfoDTO;
import com.ml.challenge.ipmetrics.clients.currency.CurrencyInfoService;
import com.ml.challenge.ipmetrics.clients.locationip.IpLocationDTO;
import com.ml.challenge.ipmetrics.clients.locationip.IpLocationService;
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
        IpInfoDTO ipInfoDTO = new IpInfoDTO();
        ipInfoDTO.setIp(ip);
        resolveIpLocation(ipInfoDTO);
        resolveCountryInfo(ipInfoDTO);
        resolveCurrencyInfo(ipInfoDTO);
        return ipInfoDTO;
    }

    private void resolveIpLocation(IpInfoDTO ipInfoDTO) {
        IpLocationDTO ipLocationDTO = ipLocationService.getIpLocation(ipInfoDTO.getIp());
            ipInfoDTO.setCountry(ipLocationDTO.getCountryName());
            ipInfoDTO.setIsoCode(ipLocationDTO.getCountryCode3());
    }

    private void resolveCountryInfo(IpInfoDTO ipInfoDTO) {
        CountryInfoDTO countryInfoDTO = countryInfoService.getCountryInfoByIsoCode(ipInfoDTO.getIsoCode());
        ipInfoDTO.setLanguages(countryInfoDTO.getLanguages().stream().map(Language::getName).collect(Collectors.toList()));
        ipInfoDTO.setTime(resolveCountryTimeZone(countryInfoDTO.getTimezones()));
        ZonedDateTime date = ipInfoDTO.getTime().stream().findFirst().map(CountryTimeZone::getTime).orElseGet(ZonedDateTime::now);
        ipInfoDTO.setCurrentDate(date.toLocalDate());
        ipInfoDTO.setCurrency(countryInfoDTO.getCurrencies().stream().findFirst().map(Currency::getCode).orElse(""));
    }

    private void resolveCurrencyInfo(IpInfoDTO ipInfoDTO) {
        CurrencyInfoDTO currencyDTO = currencyInfoService.getCurrencyInfo(ipInfoDTO.getCurrency(), "USD");
        ipInfoDTO.setCurrency(ipInfoDTO.getCurrency() + ". 1 " + ipInfoDTO.getCurrency() + " = "
                + currencyDTO.getRates().get(ipInfoDTO.getCurrency()) + " " + currencyDTO.getBase());
    }

    private List<CountryTimeZone> resolveCountryTimeZone(String[] timeZones) {
        return Stream.of(timeZones).map(utc -> new CountryTimeZone(utc, ZonedDateTime.now(ZoneId.of(utc))))
                .collect(Collectors.toList());
    }
}
