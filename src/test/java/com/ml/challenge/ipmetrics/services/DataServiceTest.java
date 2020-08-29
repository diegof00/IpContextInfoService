package com.ml.challenge.ipmetrics.services;

import com.ml.challenge.ipmetrics.dtos.IpInfoDTO;
import com.ml.challenge.ipmetrics.storage.IpInfoRepository;
import com.ml.challenge.ipmetrics.storage.IpMetricRepository;
import com.ml.challenge.ipmetrics.storage.model.IpInfo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZonedDateTime;

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
        IpInfo ipInfo = new IpInfo(1l,"1.1.1.1", "DEU", 10000D, ZonedDateTime.now());
        when(ipInfoRepository.save(any(IpInfo.class))).thenReturn(ipInfo);
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

}
