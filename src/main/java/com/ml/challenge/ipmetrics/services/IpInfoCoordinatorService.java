package com.ml.challenge.ipmetrics.services;

import com.ml.challenge.ipmetrics.clients.country.CountryInfoDTO;
import com.ml.challenge.ipmetrics.clients.country.Currency;
import com.ml.challenge.ipmetrics.clients.country.Language;
import com.ml.challenge.ipmetrics.clients.currency.CurrencyInfoDTO;
import com.ml.challenge.ipmetrics.clients.locationip.IpLocationApiClientFallback;
import com.ml.challenge.ipmetrics.clients.locationip.IpLocationDTO;
import com.ml.challenge.ipmetrics.dtos.CountryTimeZone;
import com.ml.challenge.ipmetrics.dtos.DistanceDTO;
import com.ml.challenge.ipmetrics.dtos.IpInfoDTO;
import com.ml.challenge.ipmetrics.dtos.IpMetricsResult;
import com.ml.challenge.ipmetrics.exception.IpContextInfoServiceException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class IpInfoCoordinatorService {

    private static final Logger log = LoggerFactory.getLogger(IpLocationApiClientFallback.class);
    public static final String NO_INFO_FOR_IP = "no info for ip %s";
    public static final String BASE = "USD";

    private final IpLocationService ipLocationService;
    private final CountryInfoService countryInfoService;
    private final CurrencyInfoService currencyInfoService;
    private final DistanceCalculatorService distanceCalculatorService;
    private final DataService dataService;

    public IpInfoDTO getExternalResult(String ip) {
        log.info("start resolving ip info for {}", ip);
        IpInfoDTO ipInfoDTO = new IpInfoDTO();
        ipInfoDTO.setIp(ip);
        resolveIpLocation(ipInfoDTO);
        resolveCountryInfo(ipInfoDTO);
        if (!StringUtils.isEmpty(ipInfoDTO.getCurrency())) {
            resolveCurrencyInfo(ipInfoDTO);
        }
        dataService.saveIpInfo(ipInfoDTO);
        return ipInfoDTO;
    }

    private void resolveIpLocation(IpInfoDTO ipInfoDTO) {
        IpLocationDTO ipLocationDTO = ipLocationService.getIpLocation(ipInfoDTO.getIp());
        if (StringUtils.isEmpty(ipLocationDTO.getCountryName())) {
            throw new IpContextInfoServiceException(String.format(NO_INFO_FOR_IP, ipInfoDTO.getIp()));
        }
        ipInfoDTO.setCountry(ipLocationDTO.getCountryName());
        ipInfoDTO.setIsoCode(ipLocationDTO.getCountryCode3());
    }

    private void resolveCountryInfo(IpInfoDTO ipInfoDTO) {
        CountryInfoDTO countryInfoDTO = countryInfoService.getCountryInfoByIsoCode(ipInfoDTO.getIsoCode());
        if (!StringUtils.isEmpty(countryInfoDTO.getName())) {
            ipInfoDTO.setLanguages(resolveLanguages(countryInfoDTO));
            ipInfoDTO.setTime(resolveCountryTimeZone(countryInfoDTO.getTimezones()));
            ipInfoDTO.setCurrentDate(resolveDate(ipInfoDTO));
            ipInfoDTO.setCurrency(getCurrencyCode(countryInfoDTO));
            if (isLatLngValid(countryInfoDTO)) {
                DistanceDTO distanceDTO = getDistanceToBuenosAires(countryInfoDTO);
                ipInfoDTO.setDistance(distanceDTO.getDistance());
            }
        }
    }

    private DistanceDTO getDistanceToBuenosAires(CountryInfoDTO countryInfoDTO) {
        return distanceCalculatorService.getDistanceToBuenosAires(Double.valueOf(countryInfoDTO.getLatlng()[0]),
                Double.valueOf(countryInfoDTO.getLatlng()[1]),
                countryInfoDTO.getName());
    }

    private boolean isLatLngValid(CountryInfoDTO countryInfoDTO) {
        return countryInfoDTO.getLatlng() != null && Stream.of(countryInfoDTO.getLatlng()).noneMatch(StringUtils::isEmpty);
    }

    private String getCurrencyCode(CountryInfoDTO countryInfoDTO) {
        if (countryInfoDTO.getCurrencies() != null) {
            return countryInfoDTO.getCurrencies().stream().findFirst().map(Currency::getCode).orElse("");
        }
        return "";
    }

    private LocalDate resolveDate(IpInfoDTO ipInfoDTO) {
        if (ipInfoDTO.getTime() != null) {
            return ipInfoDTO.getTime().stream().findFirst().map(CountryTimeZone::getTime).orElseGet(ZonedDateTime::now).toLocalDate();
        }
        return LocalDate.now();
    }

    private List<String> resolveLanguages(CountryInfoDTO countryInfoDTO) {
        if (countryInfoDTO.getLanguages() != null) {
            return countryInfoDTO.getLanguages().stream().map(Language::getName).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private void resolveCurrencyInfo(IpInfoDTO ipInfoDTO) {
        currencyInfoService.getCurrencyInfo(ipInfoDTO.getCurrency(), BASE).ifPresent(currencyInfo -> {
            ipInfoDTO.setCurrency(ipInfoDTO.getCurrency() + ". 1 " + ipInfoDTO.getCurrency() + " = "
                    + resolveCurrencyRate(ipInfoDTO, currencyInfo) + " " + currencyInfo.getBase());
        });
    }

    private String resolveCurrencyRate(IpInfoDTO ipInfoDTO, CurrencyInfoDTO currencyInfo) {
        return String.valueOf(currencyInfo.getRates().get(ipInfoDTO.getCurrency()));
    }

    private List<CountryTimeZone> resolveCountryTimeZone(String[] timeZones) {
        return Stream.of(timeZones).map(utc -> new CountryTimeZone(utc, ZonedDateTime.now(ZoneId.of(utc))))
                .collect(Collectors.toList());
    }

    public IpMetricsResult getMetrics() {
        return dataService.getMetrics(1L);
    }
}
