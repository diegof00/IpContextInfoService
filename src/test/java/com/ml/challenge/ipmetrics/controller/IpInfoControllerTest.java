package com.ml.challenge.ipmetrics.controller;

import com.ml.challenge.ipmetrics.dtos.IpInfoDTO;
import com.ml.challenge.ipmetrics.services.IpInfoCoordinatorService;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class IpInfoControllerTest {

    @Test
    public void shouldResolveIpInfo() {
        IpInfoCoordinatorService ipInfoCoordinatorService = Mockito.mock(IpInfoCoordinatorService.class);
        IpInfoController ipInfoController = new IpInfoController(ipInfoCoordinatorService);
        IpInfoDTO ipInfoDTO = new IpInfoDTO();
        when(ipInfoCoordinatorService.getExternalResult(anyString())).thenReturn(ipInfoDTO);
        ResponseEntity<IpInfoDTO> result = ipInfoController.getResult("10.15.5.5");
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}
