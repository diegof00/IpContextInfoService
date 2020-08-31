package com.ml.challenge.ipmetrics.services;

import com.ml.challenge.ipmetrics.dtos.IpInfoDTO;
import com.ml.challenge.ipmetrics.dtos.IpMetricDto;
import com.ml.challenge.ipmetrics.dtos.IpMetricsResult;
import com.ml.challenge.ipmetrics.storage.IpInfoRepository;
import com.ml.challenge.ipmetrics.storage.IpMetricRepository;
import com.ml.challenge.ipmetrics.storage.model.IpInfo;
import com.ml.challenge.ipmetrics.storage.model.IpMetric;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DataService {

    private static final Logger log = LoggerFactory.getLogger(DataService.class);

    private final IpInfoRepository ipInfoRepository;

    private final IpMetricRepository ipMetricRepository;

    @Async
    public void saveIpInfo(IpInfoDTO ipInfoDTO) {
        if (ipInfoDTO.getDistance() != null) {
            log.info("saving ipInfo. . . . {}, {}, {}", ipInfoDTO.getIp(), ipInfoDTO.getCountry(), ipInfoDTO.getDistance());
            IpInfo ipinfo = IpInfo.builder()
                    .ip(ipInfoDTO.getIp())
                    .country(ipInfoDTO.getCountry())
                    .distance(ipInfoDTO.getDistance())
                    .build();
            ipInfoRepository.save(ipinfo);
        }
    }

    @Cacheable(value = {"metrics-cache"}, key = "#id", unless = "#result == null || #result.averageDistance == null")
    public IpMetricsResult getMetrics(Long id) {
        List<IpMetricDto> ipMetricDtoList = ipMetricRepository.findAll().stream().map(metric -> IpMetricDto
                .builder()
                .country(metric.getCountry())
                .invocations(metric.getInvocations())
                .distance(metric.getDistance())
                .build()).collect(Collectors.toList());
        IpMetricsResult ipMetricsResult = new IpMetricsResult();
        ipMetricsResult.setIpMetricDtoList(ipMetricDtoList);
        ipMetricsResult.setId(1L);
        Double factor1 = ipMetricDtoList.stream().map(metric -> metric.getDistance() * metric.getInvocations()).reduce(0D, Double::sum);
        Long factor2 = ipMetricDtoList.stream().map(IpMetricDto::getInvocations).reduce(0L, Long::sum);
        if (factor2 > 0) {
            ipMetricsResult.setAverageDistance(String.valueOf(factor1 / factor2));
        }
        return ipMetricsResult;
    }

    @Scheduled(fixedDelay = 60_000)
    public void calculateMetrics() {
        log.info("executing scheduled method . . . ");
        List<IpMetric> ipMetric = ipMetricRepository.calculateIpMetric();
        if (ipMetric != null && !ipMetric.isEmpty()) {
            ipMetricRepository.deleteAll();
            ipMetricRepository.saveAll(ipMetric);
        }
    }

}
