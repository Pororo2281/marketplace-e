package com.marketplace.admin_service.Service;

import com.marketplace.admin_service.Client.UserClient;
import com.marketplace.admin_service.Entity.AuditLogEntity;
import com.marketplace.admin_service.Enum.AdminAction;
import com.marketplace.admin_service.Enum.TargetType;
import com.marketplace.admin_service.Repository.AuditLogRepo;
import com.marketplace.admin_service.Request.BlockUserRequest;
import com.marketplace.admin_service.Request.UnblockUserRequest;
import com.marketplace.admin_service.Response.UserResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AdminUserService {

    private final AuditLogRepo auditLogRepo;
    private final UserClient userClient;

    public AdminUserService(AuditLogRepo auditLogRepo, UserClient userClient) {
        this.auditLogRepo = auditLogRepo;
        this.userClient = userClient;
    }

    public UserResponseDto getUserById(Long id) {
           var user = userClient.getUserById(id);
           return user;
    }

    @Transactional
    public void blockUser(Long id, BlockUserRequest blockUserRequest) {
            userClient.blockUser(id);
            AuditLogEntity auditLogEntity = new AuditLogEntity();
            auditLogEntity.setTargetType(TargetType.USER);
            auditLogEntity.setAction(AdminAction.BLOCK_USER);
            auditLogEntity.setReason(blockUserRequest.getReason());
            auditLogEntity.setAdminId(blockUserRequest.getAdminId());
            auditLogEntity.setAdminEmail(blockUserRequest.getAdminEmail());
            auditLogEntity.setTargetId(id);

            auditLogRepo.save(auditLogEntity);
    }

    @Transactional
    public void unBlockUser(Long id, UnblockUserRequest unblockUserRequest) {

            userClient.unBlockUser(id);
            AuditLogEntity auditLogEntity = new AuditLogEntity();
            auditLogEntity.setTargetType(TargetType.USER);
            auditLogEntity.setAction(AdminAction.UNBLOCK_USER);
            auditLogEntity.setAdminId(unblockUserRequest.getAdminId());
            auditLogEntity.setAdminEmail(unblockUserRequest.getAdminEmail());
            auditLogEntity.setReason(unblockUserRequest.getReason());
            auditLogEntity.setTargetId(id);

            auditLogRepo.save(auditLogEntity);
    }

    public Page<UserResponseDto> getUsers(Pageable pageable, String search, String role, Boolean blocked) {
        String sortStr = pageable.getSort().toString();
        if (sortStr.isEmpty() || !sortStr.contains(":")) {
            sortStr = "createdAt,desc";
        } else {
            String[] s = sortStr.split(":");
            sortStr = s[0] + "," + s[1].trim().toLowerCase();
        }

            return userClient.getUsers(pageable.getPageNumber(),pageable.getPageSize() ,sortStr, search, role, blocked);
    }
}
