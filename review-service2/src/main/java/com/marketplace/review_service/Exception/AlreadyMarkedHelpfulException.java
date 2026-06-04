package com.marketplace.review_service.Exception;

public class AlreadyMarkedHelpfulException extends RuntimeException {
    public AlreadyMarkedHelpfulException(String message) {
        super(message);
    }
}
