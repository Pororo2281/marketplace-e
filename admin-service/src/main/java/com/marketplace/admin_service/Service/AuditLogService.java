package com.marketplace.admin_service.Service;

import com.marketplace.admin_service.Entity.AuditLogEntity;
import com.marketplace.admin_service.Enum.AdminAction;
import com.marketplace.admin_service.Enum.TargetType;
import com.marketplace.admin_service.Exception.NotFoundById;
import com.marketplace.admin_service.Mapper.AuditToResponse;
import com.marketplace.admin_service.Repository.AuditLogRepo;
import com.marketplace.admin_service.Response.AuditLogResponseDto;
import com.marketplace.admin_service.Specification.AuditSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

    private final AuditLogRepo auditLogRepo;

    public AuditLogService(AuditLogRepo auditLogRepo) {
        this.auditLogRepo = auditLogRepo;
    }


    public Page<AuditLogResponseDto> getAuditLogs(Pageable pageable, Long adminId, String actionType, String targetType) {

        AdminAction adminAction = actionType == null ? null : AdminAction.valueOf(actionType);
        TargetType type = targetType == null ? null : TargetType.valueOf(targetType);

        Specification specification = Specification.where(AuditSpecification.hasAdminAction(adminAction))
                .and(AuditSpecification.hasAdminId(adminId))
                .and(AuditSpecification.hasTargetType(type));

        Page<AuditLogEntity> auditLogs = auditLogRepo.findAll(specification,pageable);

        return auditLogs.map(AuditToResponse::from);
    }

    public AuditLogResponseDto getAuditLogById(Long id) {
        var auditLog = auditLogRepo.findById(id)
                .orElseThrow(()->new NotFoundById("Audit log not found with id: " +id));
        return AuditToResponse.from(auditLog);
    }
}
