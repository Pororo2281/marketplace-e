package com.marketplace.admin_service.Repository;

import com.marketplace.admin_service.Entity.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepo extends JpaRepository<AuditLogEntity,Long>, JpaSpecificationExecutor<AuditLogEntity> {

}
