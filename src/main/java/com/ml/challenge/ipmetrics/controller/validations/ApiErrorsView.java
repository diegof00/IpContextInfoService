package com.ml.challenge.ipmetrics.controller.validations;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ApiErrorsView {
    private List<ApiFieldError> fieldErrors;
}
