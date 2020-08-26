package com.ml.challenge.ipmetrics.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ErrorHandlerAdvice {


    @ExceptionHandler(value = ExampleServiceException.class)
    public ResponseEntity<Map<String, String>> ServiceErrorHandler(Exception exception) {
        return new ResponseEntity<>(createBody(exception, HttpStatus.NOT_FOUND.value()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> validationsErrorHandler(ConstraintViolationException exception) {
        return new ResponseEntity<>(createBody(exception, HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Map<String, String>> defaultErrorHandler(Exception exception) {
        return new ResponseEntity<>(createBody(exception, HttpStatus.INTERNAL_SERVER_ERROR.value()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private Map<String, String> createBody(Exception exception, int status) {
        Map<String, String> body = new LinkedHashMap<>();
        body.put("status", String.valueOf(status));
        body.put("message", exception.getMessage()
                .replaceAll(".*(?=exception)", "")
                .replaceAll("SQL.*SQL", ""));
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("cause", getRootCause(exception));
        if (null != exception.getCause()) {
            body.put("rootcause", sanitizeMessage((exception.getCause()).getMessage()));
        }
        return body;
    }

    private String getRootCause(Exception databaseServiceEXception) {
        return databaseServiceEXception.getCause() != null ? sanitizeMessage(databaseServiceEXception.getCause().getMessage()) : "";
    }

    private String sanitizeMessage(String causeMessage) {
        if (causeMessage.indexOf("trace") > 1) {
            causeMessage = causeMessage.substring(0, causeMessage.indexOf("trace"));
        }
        return causeMessage;
    }

}
