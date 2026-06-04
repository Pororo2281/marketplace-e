package com.marketplace.review_service.Exception;

public class ReviewCannotBeApprovedException extends RuntimeException {
    public ReviewCannotBeApprovedException(String message) {
        super(message);
    }
}
