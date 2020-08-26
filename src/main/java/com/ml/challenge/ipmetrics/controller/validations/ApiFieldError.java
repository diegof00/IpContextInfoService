package com.ml.challenge.ipmetrics.controller.validations;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
@AllArgsConstructor
public class ApiFieldError {
    private String field;
    private String code;
    private String message;
}
