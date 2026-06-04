package com.marketplace.review_service.Exception;

public class ReviewAlreadyExist extends RuntimeException {
    public ReviewAlreadyExist(String message) {
        super(message);
    }
}
