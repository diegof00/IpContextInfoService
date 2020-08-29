package com.ml.challenge.ipmetrics.services;

import com.ml.challenge.ipmetrics.dtos.DistanceDTO;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DistanceCalculatorServiceTest {

    private DistanceCalculatorService distanceCalculatorService;

    @Test
    public void shouldCalculateDistance() {
        distanceCalculatorService = new DistanceCalculatorService();
        Double latitude1 = 1D,  latitude2 = 1D, longitude1 = 0D, longitude2 = 0D;
        DistanceDTO result = distanceCalculatorService.getDistanceToBuenosAires(latitude1, longitude1, "USA");
        assertNotNull(result);
        assertEquals("USA", result.getCountryName());
        assertEquals(Double.valueOf(7235.251934200906D), result.getDistance());
    }


}
