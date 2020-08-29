package com.ml.challenge.ipmetrics.clients.locationip;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "${external.service.ipinfo.name}", url = "${external.service.ipinfo.url}", fallback = IpLocationApiClientFallback.class)
public interface IpLocationApiClient {


    @GetMapping(value = "/ip?{ip}", produces = "application/json")
    Optional<IpLocationDTO> getIpLocation(@PathVariable String ip);
}
