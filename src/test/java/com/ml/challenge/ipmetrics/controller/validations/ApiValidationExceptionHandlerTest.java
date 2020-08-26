package com.ml.challenge.ipmetrics.controller.validations;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ApiValidationExceptionHandlerTest {

    private ApiValidationExceptionHandler apiValidationExceptionHandler;

    @Test
    public void test() {
        apiValidationExceptionHandler = new ApiValidationExceptionHandler();
        BindingResult binding = Mockito.mock(BindingResult.class);
        MethodParameter parameter = Mockito.mock(MethodParameter.class);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(parameter, binding);
        List<FieldError> errors = Arrays.asList(new FieldError("name", "fieldName",
                null, true, new String[]{"code"}, null, "message"));
        when(binding.getFieldErrors()).thenReturn(errors);
        HttpHeaders headers = null;
        WebRequest request = null;
        HttpStatus status = null;
        ResponseEntity<Object> result = apiValidationExceptionHandler.handleMethodArgumentNotValid(exception, headers, status, request);
        assertEquals("fieldName", ((ApiErrorsView) result.getBody()).getFieldErrors().get(0).getField());
        assertEquals("message", ((ApiErrorsView) result.getBody()).getFieldErrors().get(0).getMessage());
        assertEquals("code", ((ApiErrorsView) result.getBody()).getFieldErrors().get(0).getCode());
    }


}
