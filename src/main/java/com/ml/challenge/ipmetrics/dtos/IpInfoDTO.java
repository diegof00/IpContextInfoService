package com.ml.challenge.ipmetrics.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@Builder
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


/*
    Información de salida:
    IP: 83.44.196.93
    Fecha actual: 21/11/2016 16:01:23
    País: España (spain)
    ISO Code: es
    Idiomas: Español (es)
    Moneda: EUR (1 EUR = 1.0631 U$S)
    Hora: 20:01:23 (UTC) o 21:01:23 (UTC+01:00)
    Distancia estimada: 10270 km. (-34, -64) a (40, -4) */

}
