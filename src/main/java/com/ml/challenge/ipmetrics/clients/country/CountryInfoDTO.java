package com.ml.challenge.ipmetrics.clients.country;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CountryInfoDTO {

    private String name;
    private String code;
    private List<Language> languages;
    private List<Currency> currencies;
}
