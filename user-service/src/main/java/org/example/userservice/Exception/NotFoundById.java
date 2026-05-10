package org.example.userservice.Exception;

public class NotFoundById extends RuntimeException {
    public NotFoundById(String message) {
        super(message);
    }
}
