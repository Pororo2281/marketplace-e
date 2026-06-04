package com.marketplace.admin_service.Controller;

import com.marketplace.admin_service.Request.ApproveReviewRequest;
import com.marketplace.admin_service.Request.DeleteReviewRequest;
import com.marketplace.admin_service.Request.RejectReviewRequest;
import com.marketplace.admin_service.Response.ReviewResponseDto;
import com.marketplace.admin_service.Service.AdminReviewService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admins/review")
public class AdminReviewController {

    private final AdminReviewService adminReviewService;

    public AdminReviewController(AdminReviewService adminReviewService) {
        this.adminReviewService = adminReviewService;
    }

    @GetMapping("/pending")
    public ResponseEntity<Page<ReviewResponseDto>> getReviews(@PageableDefault(size = 20) Pageable pageable){
        return ResponseEntity.ok(adminReviewService.getReviews(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> getReviewById(@PathVariable Long id){
        return ResponseEntity.ok(adminReviewService.getReviewById(id));
    }

    @PatchMapping("/{id}/approve")
    public ResponseEntity<ReviewResponseDto> approveReview(@Valid @RequestBody ApproveReviewRequest approveReviewRequest, @PathVariable Long id){
        return ResponseEntity.ok(adminReviewService.approveReview(id, approveReviewRequest));
    }

    @PatchMapping("/{id}/reject")
    public ResponseEntity<ReviewResponseDto> rejectReview(@Valid @RequestBody RejectReviewRequest rejectReviewRequest, @PathVariable Long id){
        return ResponseEntity.ok(adminReviewService.rejectReview(id, rejectReviewRequest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> deleteReview(@Valid @RequestBody DeleteReviewRequest deleteReviewRequest, @PathVariable Long id){
        adminReviewService.deleteReview(id, deleteReviewRequest);
        return ResponseEntity.noContent().build();
    }

}
