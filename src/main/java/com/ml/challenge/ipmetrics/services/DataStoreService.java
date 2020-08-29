package com.ml.challenge.ipmetrics.services;

import com.ml.challenge.ipmetrics.dtos.IpInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class DataStoreService {

    private static final Logger log = LoggerFactory.getLogger(DataStoreService.class);

    @Async
    public void saveDto(IpInfoDTO ipInfoDTO) {
        log.info("saving message. . . . ");
        try {
            Thread.sleep(1000);
            log.info("message. . . .saved ");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
