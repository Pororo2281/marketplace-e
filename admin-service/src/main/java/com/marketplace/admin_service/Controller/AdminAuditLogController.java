package com.marketplace.admin_service.Controller;

import com.marketplace.admin_service.Response.AuditLogResponseDto;
import com.marketplace.admin_service.Service.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admins/audit-log")
public class AdminAuditLogController {

    private final AuditLogService service;

    public AdminAuditLogController(AuditLogService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<AuditLogResponseDto>> getAuditLogs(@PageableDefault(size = 20) Pageable pageable,
                                                                  @RequestParam(required = false) Long adminId,
                                                                  @RequestParam(required = false) String actionType,
                                                                  @RequestParam(required = false) String targetType){
        return ResponseEntity.ok(service.getAuditLogs(pageable,adminId,actionType,targetType));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditLogResponseDto> getAuditLog(@PathVariable Long id){
        return ResponseEntity.ok(service.getAuditLogById(id));
    }

}
