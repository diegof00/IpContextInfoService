package com.ml.challenge.ipmetrics.dtos;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class IpMetricsResult implements Serializable {

    private Long id;

    private List<IpMetricDto> ipMetricDtoList;

    private String averageDistance;

}
