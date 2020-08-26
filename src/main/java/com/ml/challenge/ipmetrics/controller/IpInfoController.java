package com.ml.challenge.ipmetrics.controller;

import com.ml.challenge.ipmetrics.dtos.IpInfoDTO;
import com.ml.challenge.ipmetrics.services.IpInfoCoordinatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@RestController
@RequestMapping("/api/ip/")
@Validated
public class IpInfoController {

    private static final String IP_PATERN = "^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$";
    private static final String ERROR_MESSAGE = "The parameter should be an IP address";
    private final IpInfoCoordinatorService ipInfoCoordinatorService;

    @Autowired
    public IpInfoController(IpInfoCoordinatorService ipInfoCoordinatorService) {
        this.ipInfoCoordinatorService = ipInfoCoordinatorService;
    }

    @GetMapping("/{ip}")
    public @ResponseBody ResponseEntity<IpInfoDTO> getResult(@PathVariable @Valid @Pattern(regexp = IP_PATERN, message = ERROR_MESSAGE) String ip) {
        return ResponseEntity.ok(ipInfoCoordinatorService.getExternalResult(ip));
    }
}
