package com.marketplace.admin_service.Response;


import com.marketplace.admin_service.Entity.AuditLogEntity;
import com.marketplace.admin_service.Enum.AdminAction;
import com.marketplace.admin_service.Enum.TargetType;

import java.time.Instant;

public class AuditLogResponseDto {

    private Long id;
    private Long adminId;
    private String adminEmail;
    private AdminAction action;
    private TargetType targetType;
    private Long targetId;
    private String reason;
    private String details;
    private Instant createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAdminId() { return adminId; }
    public void setAdminId(Long adminId) { this.adminId = adminId; }

    public String getAdminEmail() { return adminEmail; }
    public void setAdminEmail(String adminEmail) { this.adminEmail = adminEmail; }

    public AdminAction getAction() { return action; }
    public void setAction(AdminAction action) { this.action = action; }

    public TargetType getTargetType() { return targetType; }
    public void setTargetType(TargetType targetType) { this.targetType = targetType; }

    public Long getTargetId() { return targetId; }
    public void setTargetId(Long targetId) { this.targetId = targetId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
