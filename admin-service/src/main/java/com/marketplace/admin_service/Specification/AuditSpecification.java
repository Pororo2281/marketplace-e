package com.marketplace.admin_service.Specification;

import com.marketplace.admin_service.Entity.AuditLogEntity;
import com.marketplace.admin_service.Enum.AdminAction;
import com.marketplace.admin_service.Enum.TargetType;
import org.springframework.data.jpa.domain.Specification;

import java.awt.*;

public class AuditSpecification {

    public static Specification<AuditLogEntity> hasAdminId(Long adminId) {
        return (root, query, criteriaBuilder) -> {
            if (adminId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("adminId"), adminId);
        };
    }

    public static Specification<AuditLogEntity> hasAdminAction(AdminAction adminAction) {
        return (root, query, criteriaBuilder) -> {
            if (adminAction == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("action"), adminAction);
        };
    }
    public static Specification<AuditLogEntity> hasTargetType(TargetType targetType) {
        return (root, query, criteriaBuilder) -> {
            if (targetType == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("targetType"), targetType);
        };
    }

}
