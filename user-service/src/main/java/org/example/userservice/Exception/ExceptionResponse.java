package org.example.userservice.Exception;

import java.time.Instant;

public class ExceptionResponse {
    private String message;
    private Integer statusCode;
    private Instant timestamp;

    public ExceptionResponse(String message, Integer statusCode, Instant timestamp) {
        this.message = message;
        this.statusCode = statusCode;
        this.timestamp = timestamp;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
