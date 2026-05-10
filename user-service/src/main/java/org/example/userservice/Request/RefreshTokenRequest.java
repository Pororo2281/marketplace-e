package org.example.userservice.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RefreshTokenRequest {

    @NotBlank(message = "Refresh token is required")
    @Size(max = 1000, message = "Refresh token is too long")
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
