package org.example.userservice.Response;

public class OAuth2UserResponse {
    private Long userId;
    private String role;

    public OAuth2UserResponse(Long userId, String role) {
        this.userId = userId;
        this.role = role;
    }

    public OAuth2UserResponse() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
