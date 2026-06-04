package com.marketplace.review_service.Exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}
