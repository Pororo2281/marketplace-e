package com.marketplace.admin_service.Request;


import jakarta.validation.constraints.NotNull;

public class ApproveProductRequest {

    @NotNull(message = "adminId is required")
    private Long adminId;

    private String adminEmail;


    public Long getAdminId() { return adminId; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }

    public String getAdminEmail() { return adminEmail; }
    public void setAdminEmail(String adminEmail) { this.adminEmail = adminEmail; }
}