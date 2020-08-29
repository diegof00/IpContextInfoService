package com.ml.challenge.ipmetrics.clients.country;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;


@Getter
@Setter
public class CountryInfoDTO implements Serializable {

    private String name;
    private String code;
    private List<Language> languages;
    private List<Currency> currencies;
    private String[] latlng;
    private String[] timezones;
}
