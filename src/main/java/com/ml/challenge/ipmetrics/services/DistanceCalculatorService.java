package com.ml.challenge.ipmetrics.services;

import com.ml.challenge.ipmetrics.dtos.DistanceDTO;
import org.apache.lucene.util.SloppyMath;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DistanceCalculatorService {

    private static final double LAT_BUENOS_AIRES = -34.603722;
    private static final double LON_BUENOS_AIRES = -58.381592;

    @Cacheable(value = {"distance-cache"}, key = "#countryName", unless = "#result == null")
    public DistanceDTO getDistanceToBuenosAires(Double latitude, Double longitude, String countryName) {
        DistanceDTO distanceDTO = new DistanceDTO();
        distanceDTO.setDistance(SloppyMath.haversinMeters(LAT_BUENOS_AIRES, LON_BUENOS_AIRES, latitude, longitude) / 1000);
        distanceDTO.setCountryName(countryName);
        return distanceDTO;
    }

}
