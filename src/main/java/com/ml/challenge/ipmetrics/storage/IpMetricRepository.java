package com.ml.challenge.ipmetrics.storage;

import com.ml.challenge.ipmetrics.storage.model.IpMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IpMetricRepository extends JpaRepository<IpMetric, Long> {

    @Query(value = "select country as country,  count(*) as invocations, distance as distance from ip_info group by country, distance;" , nativeQuery = true)
    List<IpMetric> calculateIpMetric();

}
