package com.ml.challenge.ipmetrics.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
@Setter
public class CountryTimeZone implements Serializable {
    private String utc;
    private ZonedDateTime time;
}
