package com.ml.challenge.ipmetrics.services;

import com.ml.challenge.ipmetrics.clients.country.CountryInfoDTO;
import com.ml.challenge.ipmetrics.clients.currency.CurrencyInfoDTO;
import com.ml.challenge.ipmetrics.clients.locationip.IpLocationDTO;
import com.ml.challenge.ipmetrics.dtos.IpInfoDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class IpInfoCoordinatorService {

    private IpLocationService ipLocationService;
    private CountryInfoService countryInfoService;
    private CurrencyInfoService currencyInfoService;

    public IpInfoDTO getExternalResult(String ip) {
        IpLocationDTO ipLocationDTO = ipLocationService.getIpLocation(ip);
        IpInfoDTO ipInfoDTO = new IpInfoDTO();
        ipInfoDTO.setIp(ip);
        ipInfoDTO.setCountry(ipLocationDTO.getCountryName());
        ipInfoDTO.setIsoCode(ipLocationDTO.getCountryCode3());
        CountryInfoDTO countryInfoDTO = countryInfoService.getCountryInfoByIsoCode(ipInfoDTO.getIsoCode());
        ipInfoDTO.setLanguages(countryInfoDTO.getLanguages().stream().map(languages -> languages.getName()).collect(Collectors.toList()));
        CurrencyInfoDTO currencyDTO = currencyInfoService.getCurrencyInfo(countryInfoDTO.getCurrencies().get(0).getCode(), "USD");
        ipInfoDTO.setCurrency(countryInfoDTO.getCurrencies().get(0).getCode() + " 1" + countryInfoDTO.getCurrencies().get(0).getCode() + " = "
                + currencyDTO.getRates().get(countryInfoDTO.getCurrencies().get(0).getCode()) + " " + currencyDTO.getBase());
        return ipInfoDTO;
    }
}
