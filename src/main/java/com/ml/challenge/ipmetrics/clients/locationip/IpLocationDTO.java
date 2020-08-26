package com.ml.challenge.ipmetrics.clients.locationip;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class IpLocationDTO implements Serializable {
    private String countryCode;
    private String countryCode3;
    private String countryName;

}
