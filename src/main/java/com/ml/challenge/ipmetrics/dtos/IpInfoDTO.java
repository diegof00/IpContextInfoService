package com.ml.challenge.ipmetrics.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IpInfoDTO implements Serializable {

    @JsonAlias("IP")
    private String ip;

    private LocalDate currentDate;

    private List<CountryTimeZone> time;

    private String country;

    private String isoCode;

    private List<String> languages;

    private String currency;

    private String distance;

}
