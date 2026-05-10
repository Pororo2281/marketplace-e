package org.example.userservice.Response;

import java.util.Map;

public class AuthResponse {
    private String token;
    private String refreshToken;
    private String tokenType;
    private String email;
    private String role;




    public AuthResponse(String token, String refreshToken, String email, String role) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.tokenType = "Bearer";
        this.email = email;
        this.role = role;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
