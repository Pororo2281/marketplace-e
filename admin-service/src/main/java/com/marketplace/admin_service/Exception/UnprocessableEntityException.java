package com.marketplace.admin_service.Exception;

public class UnprocessableEntityException extends RuntimeException {
    public UnprocessableEntityException(String message,Throwable cause) {
        super(message,cause);
    }
}
