package com.example.Security.Exception;

public class NoReviewsFoundException extends RuntimeException {
    public NoReviewsFoundException(String message) {
        super(message);
    }
}
