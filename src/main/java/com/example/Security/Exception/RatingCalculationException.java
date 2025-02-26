package com.example.Security.Exception;

public class RatingCalculationException extends RuntimeException {
    public RatingCalculationException(String message, Throwable cause) {
        super(message, cause);
    }
}
