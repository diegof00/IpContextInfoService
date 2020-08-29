package com.ml.challenge.ipmetrics.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class IpMetricDto implements Serializable {

    private String country;

    private Double distance;

    private Long invocations;

}
