package com.marketplace.admin_service.Service;

import com.marketplace.admin_service.Client.ProductClient;
import com.marketplace.admin_service.Entity.AuditLogEntity;
import com.marketplace.admin_service.Enum.AdminAction;
import com.marketplace.admin_service.Enum.TargetType;
import com.marketplace.admin_service.Repository.AuditLogRepo;
import com.marketplace.admin_service.Request.ApproveProductRequest;
import com.marketplace.admin_service.Request.RejectProductRequest;
import com.marketplace.admin_service.Response.ProductResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AdminProductService {

    private final AuditLogRepo auditLogRepo;
    private final ProductClient productClient;

    public AdminProductService(AuditLogRepo auditLogRepo, ProductClient productClient) {
        this.auditLogRepo = auditLogRepo;
        this.productClient = productClient;
    }

    public Page<ProductResponseDto> getPendingProducts(Pageable pageable) {
        String sortStr = pageable.getSort().toString();
        if (sortStr.isEmpty() || !sortStr.contains(":")) {
            sortStr = "createdAt,desc";
        } else {
            String[] s = sortStr.split(":");
            sortStr = s[0] + "," + s[1].trim().toLowerCase();
        }
        return productClient.getProducts(pageable.getPageNumber(),pageable.getPageSize(),sortStr);
    }

    @Transactional
    public void approveProduct(Long id, ApproveProductRequest approveProductRequest) {

            productClient.approveProduct(id);
            AuditLogEntity auditLogEntity = new AuditLogEntity();
            auditLogEntity.setAction(AdminAction.APPROVE_PRODUCT);
            auditLogEntity.setTargetType(TargetType.PRODUCT);
            auditLogEntity.setAdminEmail(approveProductRequest.getAdminEmail());
            auditLogEntity.setAdminId(approveProductRequest.getAdminId());
            auditLogEntity.setTargetId(id);

            auditLogRepo.save(auditLogEntity);
    }

    public ProductResponseDto getProductById(Long id) {
         var product =  productClient.getProduct(id);
         return product;
    }

    @Transactional
    public void rejectProduct(Long id, RejectProductRequest rejectProductRequest) {
            productClient.rejectProduct(id);
            AuditLogEntity auditLogEntity = new AuditLogEntity();
            auditLogEntity.setAction(AdminAction.REJECT_PRODUCT);
            auditLogEntity.setTargetType(TargetType.PRODUCT);
            auditLogEntity.setAdminEmail(rejectProductRequest.getAdminEmail());
            auditLogEntity.setAdminId(rejectProductRequest.getAdminId());
            auditLogEntity.setReason(rejectProductRequest.getReason());
            auditLogEntity.setTargetId(id);

            auditLogRepo.save(auditLogEntity);
    }
}
