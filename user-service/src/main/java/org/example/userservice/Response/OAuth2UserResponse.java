package org.example.userservice.Response;

public class OAuth2UserResponse {

    private Long userId;
    private String role;
    private String status;

    public OAuth2UserResponse(Long userId, String role, String status) {
        this.userId = userId;
        this.role = role;
        this.status = status;
    }

    public OAuth2UserResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
