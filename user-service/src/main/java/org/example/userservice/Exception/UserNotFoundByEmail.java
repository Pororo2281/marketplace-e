package org.example.userservice.Exception;

public class UserNotFoundByEmail extends RuntimeException {
    public UserNotFoundByEmail(String message) {
        super(message);
    }
}
