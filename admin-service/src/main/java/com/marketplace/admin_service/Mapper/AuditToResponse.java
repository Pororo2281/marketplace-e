package com.marketplace.admin_service.Mapper;

import com.marketplace.admin_service.Entity.AuditLogEntity;
import com.marketplace.admin_service.Response.AuditLogResponseDto;

public class AuditToResponse {
    public static AuditLogResponseDto from(AuditLogEntity entity) {
        AuditLogResponseDto dto = new AuditLogResponseDto();
        dto.setId(entity.getId());
        dto.setAdminId(entity.getAdminId());
        dto.setAdminEmail(entity.getAdminEmail());
        dto.setAction(entity.getAction());
        dto.setTargetType(entity.getTargetType());
        dto.setTargetId(entity.getTargetId());
        dto.setReason(entity.getReason());
        dto.setDetails(entity.getDetails());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }
}
