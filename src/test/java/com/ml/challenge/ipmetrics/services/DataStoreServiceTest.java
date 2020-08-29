package com.ml.challenge.ipmetrics.services;

import com.ml.challenge.ipmetrics.dtos.IpInfoDTO;
import org.junit.Test;

public class DataStoreServiceTest {


    @Test
    public void test() {
        DataStoreService dataStoreService = new DataStoreService();
        dataStoreService.saveDto(new IpInfoDTO());
    }

}
