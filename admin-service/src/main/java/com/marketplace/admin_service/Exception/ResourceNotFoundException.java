package com.marketplace.admin_service.Exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message,Throwable cause) {
        super(message,cause);
    }
}
