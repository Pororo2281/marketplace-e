package com.marketplace.review_service.Exception;

public class ReviewCannotBeRejectedException extends RuntimeException {
    public ReviewCannotBeRejectedException(String message) {
        super(message);
    }
}
