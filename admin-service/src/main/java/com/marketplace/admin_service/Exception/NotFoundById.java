package com.marketplace.admin_service.Exception;

public class NotFoundById extends RuntimeException {
    public NotFoundById(String message) {
        super(message);
    }
}
