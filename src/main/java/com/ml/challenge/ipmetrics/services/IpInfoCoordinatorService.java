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

import java.time.ZoneId;
import java.time.ZonedDateTime;
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
            ipInfoDTO.setLanguages(countryInfoDTO.getLanguages().stream().map(Language::getName).collect(Collectors.toList()));
            ipInfoDTO.setTime(resolveCountryTimeZone(countryInfoDTO.getTimezones()));
            ZonedDateTime date = ipInfoDTO.getTime().stream().findFirst().map(CountryTimeZone::getTime).orElseGet(ZonedDateTime::now);
            ipInfoDTO.setCurrentDate(date.toLocalDate());
            ipInfoDTO.setCurrency(countryInfoDTO.getCurrencies().stream().findFirst().map(Currency::getCode).orElse(""));
            if (countryInfoDTO.getLatlng() != null && Stream.of(countryInfoDTO.getLatlng()).noneMatch(StringUtils::isEmpty)) {
                DistanceDTO distanceDTO = distanceCalculatorService.getDistanceToBuenosAires
                        (Double.valueOf(countryInfoDTO.getLatlng()[0]), Double.valueOf(countryInfoDTO.getLatlng()[1]), countryInfoDTO.getName());
                ipInfoDTO.setDistance(distanceDTO.getDistance());
            }
        }
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
