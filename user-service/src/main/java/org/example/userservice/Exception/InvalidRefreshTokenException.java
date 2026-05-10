package org.example.userservice.Exception;

public class InvalidRefreshTokenException extends RuntimeException {
    public InvalidRefreshTokenException(String message) {
        super(message);
    }
}
