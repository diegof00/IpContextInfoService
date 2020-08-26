package com.ml.challenge.ipmetrics.clients.country;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class Language implements Serializable {
    private String name;
    private String nativeName;
}