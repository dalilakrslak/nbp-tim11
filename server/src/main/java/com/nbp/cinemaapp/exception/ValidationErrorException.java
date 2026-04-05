package com.nbp.cinemaapp.exception;

import java.util.Map;

public class ValidationErrorException extends RuntimeException {
    private final Map<String, String> fieldErrors;

    public ValidationErrorException(final Map<String, String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
