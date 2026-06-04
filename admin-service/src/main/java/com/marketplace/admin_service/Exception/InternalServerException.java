package com.marketplace.admin_service.Exception;

public class InternalServerException extends RuntimeException {
    public InternalServerException(String message,Throwable cause) {
        super(message,cause);
    }
}
