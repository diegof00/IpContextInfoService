package com.ml.challenge.ipmetrics.services;

import com.ml.challenge.ipmetrics.dtos.IpInfoDTO;
import com.ml.challenge.ipmetrics.dtos.IpMetricsResult;
import com.ml.challenge.ipmetrics.storage.IpInfoRepository;
import com.ml.challenge.ipmetrics.storage.IpMetricRepository;
import com.ml.challenge.ipmetrics.storage.model.IpInfo;
import com.ml.challenge.ipmetrics.storage.model.IpMetric;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DataServiceTest {


    @Mock
    private IpInfoRepository ipInfoRepository;

    @Mock
    private IpMetricRepository ipMetricRepository;

    private DataService dataService;

    private IpInfoDTO ipInfoDto;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ipInfoDto = new IpInfoDTO();
        ipInfoDto.setCountry("COUNTRY");
        ipInfoDto.setDistance(4000.215161D);
        ipInfoDto.setIp("1.1.1.1");
        dataService = new DataService(ipInfoRepository, ipMetricRepository);
        IpInfo ipInfo = new IpInfo(1L,"1.1.1.1", "DEU", 10000D, ZonedDateTime.now());
        when(ipInfoRepository.save(any(IpInfo.class))).thenReturn(ipInfo);
    }

    private List<IpMetric> getIpMetrics(){
        return Arrays.asList(IpMetric.builder()
                        .country("COLOMBIA")
                        .distance(23500.20)
                        .invocations(20L).build(),
                IpMetric.builder()
                        .country("Australia")
                        .distance(13061.08525474750)
                        .invocations(5L).build(),
                IpMetric.builder()
                        .country("Brazil")
                        .distance(2757.3310919779183)
                        .invocations(23L).build());
    }

    @Test
    public void shouldSaveIpInfo() {
        dataService.saveIpInfo(ipInfoDto);
        verify(ipInfoRepository, times(1)).save(any(IpInfo.class));
    }

    @Test
    public void shouldNotSaveIpInfo() {
        dataService.saveIpInfo(new IpInfoDTO());
        verify(ipInfoRepository, never()).save(any(IpInfo.class));
    }

    @Test
    public void getMetric() {
        when(ipMetricRepository.findAll()).thenReturn(getIpMetrics());
        IpMetricsResult ipMetricsResult = dataService.getMetrics(1L);
        Assert.assertEquals(ipMetricsResult.getAverageDistance(), "12473.500862275616");
        Assert.assertEquals(3, ipMetricsResult.getIpMetricDtoList().size());
        Assert.assertEquals(1L, (long) ipMetricsResult.getId());

    }

    @Test
    public void getEmptyMetric() {
        when(ipMetricRepository.findAll()).thenReturn(new ArrayList<>());
        IpMetricsResult ipMetricsResult = dataService.getMetrics(1L);
        Assert.assertNull(ipMetricsResult.getAverageDistance());
        Assert.assertEquals(0, ipMetricsResult.getIpMetricDtoList().size());
        Assert.assertEquals(1L, (long) ipMetricsResult.getId());

    }

    @Test
    public void calculateMetrics() {
        when(ipMetricRepository.calculateIpMetric()).thenReturn(getIpMetrics());
        dataService.calculateMetrics();
        verify(ipMetricRepository).deleteAll();
        verify(ipMetricRepository).saveAll(any(List.class));
    }
    @Test
    public void calculateEmptyMetrics() {
        when(ipMetricRepository.calculateIpMetric()).thenReturn(new ArrayList<>());
        dataService.calculateMetrics();
        verify(ipMetricRepository, never()).deleteAll();
        verify(ipMetricRepository, never()).saveAll(any(List.class));
    }
}
