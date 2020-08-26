package com.ml.challenge.ipmetrics.exception;

public class ExampleServiceException extends RuntimeException {

    public ExampleServiceException(String message, Throwable e) {
        super(message, e);
    }
}
