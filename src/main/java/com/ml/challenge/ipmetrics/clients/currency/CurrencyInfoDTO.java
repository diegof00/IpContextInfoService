package com.ml.challenge.ipmetrics.clients.currency;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CurrencyInfoDTO {

    private Map<String, Float> rates;
    private String base;
    private String date;

}
