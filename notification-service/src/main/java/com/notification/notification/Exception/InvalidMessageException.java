package com.notification.notification.Exception;

public class InvalidMessageException extends RuntimeException {
    public InvalidMessageException(String message,Throwable cause) {
        super(message,cause);
    }
}
