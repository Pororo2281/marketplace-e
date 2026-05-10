package org.example.userservice.Exception;

public class SellerProfileExists extends RuntimeException {
    public SellerProfileExists(String message) {
        super(message);
    }
}
