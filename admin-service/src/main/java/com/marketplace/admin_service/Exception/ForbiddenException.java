package com.marketplace.admin_service.Exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message,Throwable cause) {
        super(message,cause);
    }
}
