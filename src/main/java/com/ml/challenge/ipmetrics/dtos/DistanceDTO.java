package com.ml.challenge.ipmetrics.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class DistanceDTO implements Serializable {

    private String countryName;
    private String distance;

}
