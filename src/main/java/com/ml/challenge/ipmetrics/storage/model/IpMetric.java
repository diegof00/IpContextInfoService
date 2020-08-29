package com.ml.challenge.ipmetrics.storage.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ip_metric")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IpMetric {

    @Id
    private String country;
    private Long invocations;
    private Double distance;


}
