package com.marketplace.admin_service.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DeleteReviewRequest {

    @NotNull(message = "adminId is required")
    private Long adminId;

    private String adminEmail;

    @NotBlank(message = "reason is required")
    private String reason;


    public Long getAdminId() { return adminId; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }

    public String getAdminEmail() { return adminEmail; }
    public void setAdminEmail(String adminEmail) { this.adminEmail = adminEmail; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
