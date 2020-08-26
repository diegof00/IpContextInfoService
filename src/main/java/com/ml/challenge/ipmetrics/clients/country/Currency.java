package com.ml.challenge.ipmetrics.clients.country;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Currency implements Serializable {
    private String code;
    private String name;
    private String symbol;
}
