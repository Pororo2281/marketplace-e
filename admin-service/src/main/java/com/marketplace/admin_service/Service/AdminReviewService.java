package com.marketplace.admin_service.Service;

import com.marketplace.admin_service.Client.ReviewClient;
import com.marketplace.admin_service.Entity.AuditLogEntity;
import com.marketplace.admin_service.Enum.AdminAction;
import com.marketplace.admin_service.Enum.TargetType;
import com.marketplace.admin_service.Repository.AuditLogRepo;
import com.marketplace.admin_service.Request.ApproveReviewRequest;
import com.marketplace.admin_service.Request.DeleteReviewRequest;
import com.marketplace.admin_service.Request.RejectReviewRequest;
import com.marketplace.admin_service.Response.ReviewResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AdminReviewService {

    private final AuditLogRepo auditLogRepo;
    private final ReviewClient reviewClient;

    public AdminReviewService(AuditLogRepo auditLogRepo, ReviewClient reviewClient) {
        this.auditLogRepo = auditLogRepo;
        this.reviewClient = reviewClient;
    }

    public ReviewResponseDto getReviewById(Long id) {
        return  reviewClient.getReviewById(id);
    }


    @Transactional
    public ReviewResponseDto approveReview(Long id, ApproveReviewRequest approveReviewRequest) {
        AuditLogEntity auditLogEntity = new AuditLogEntity();
        auditLogEntity.setAdminId(approveReviewRequest.getAdminId());
        auditLogEntity.setAction(AdminAction.APPROVE_REVIEW);
        auditLogEntity.setTargetType(TargetType.REVIEW);
        auditLogEntity.setAdminEmail(approveReviewRequest.getAdminEmail());
        auditLogRepo.save(auditLogEntity);

        return reviewClient.approveReview(id);

    }

    @Transactional
    public ReviewResponseDto rejectReview(Long id , RejectReviewRequest rejectReviewRequest) {

        AuditLogEntity auditLogEntity = new AuditLogEntity();
        auditLogEntity.setAdminId(rejectReviewRequest.getAdminId());
        auditLogEntity.setAction(AdminAction.APPROVE_REVIEW);
        auditLogEntity.setTargetType(TargetType.REVIEW);
        auditLogEntity.setAdminEmail(rejectReviewRequest.getAdminEmail());
        auditLogEntity.setReason(rejectReviewRequest.getReason());
        auditLogEntity.setTargetId(id);
        auditLogRepo.save(auditLogEntity);

        return reviewClient.rejectReview(id);
    }

    @Transactional
    public void deleteReview(Long id, DeleteReviewRequest deleteReviewRequest) {

        AuditLogEntity auditLogEntity = new AuditLogEntity();
        auditLogEntity.setAdminId(deleteReviewRequest.getAdminId());
        auditLogEntity.setAction(AdminAction.DELETE_REVIEW);
        auditLogEntity.setTargetType(TargetType.REVIEW);
        auditLogEntity.setAdminEmail(deleteReviewRequest.getAdminEmail());
        auditLogEntity.setReason(deleteReviewRequest.getReason());
        auditLogEntity.setTargetId(id);
        auditLogRepo.save(auditLogEntity);

        reviewClient.deleteReview(id);
    }

    public Page<ReviewResponseDto> getReviews(Pageable pageable) {
        String sortStr = pageable.getSort().toString();
        if (sortStr.isEmpty() || !sortStr.contains(":")) {
            sortStr = "createdAt,desc";
        } else {
            String[] s = sortStr.split(":");
            sortStr = s[0] + "," + s[1].trim().toLowerCase();
        }

        return reviewClient.getPendingReviews(pageable.getPageNumber(),pageable.getPageSize(),sortStr);
    }
}
