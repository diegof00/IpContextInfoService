package com.ml.challenge.ipmetrics.error;

import com.ml.challenge.ipmetrics.exception.ErrorHandlerAdvice;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

public class ErrorHandlerAdviceTest {

    private final ErrorHandlerAdvice errorHandlerAdvice = new ErrorHandlerAdvice();

    @Test
    public void shouldReturnInternalServerErrorWhenExceptionOccursWithNestedException() {
        ResponseEntity<Map<String, String>> result = errorHandlerAdvice.defaultErrorHandler(
                new Exception("exception message", new RuntimeException("nested trace: concurrentException")));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("exception message", Objects.requireNonNull(result.getBody()).get("message"));
        assertEquals("nested ", result.getBody().get("cause"));
    }

    @Test
    public void shouldReturnInternalServerErrorWhenExceptionOccurs() {
        ResponseEntity<Map<String, String>> result = errorHandlerAdvice.defaultErrorHandler(
                new Exception("exception message"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals("exception message", Objects.requireNonNull(result.getBody()).get("message"));
    }


}
