package org.example.userservice.Exception;

public class UserBlocked extends RuntimeException {
    public UserBlocked(String message) {
        super(message);
    }
}
