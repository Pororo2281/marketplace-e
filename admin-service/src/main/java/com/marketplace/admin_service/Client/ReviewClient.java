package com.marketplace.admin_service.Client;

import com.marketplace.admin_service.Response.ReviewResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "review-service",url = "${services.review-service:http://localhost:8088}")
public interface ReviewClient {

    @GetMapping("/api/reviews/{reviewId}")
    ReviewResponseDto getReviewById(@PathVariable Long reviewId);

    @PatchMapping("api/reviews/admin/{reviewId}/approve")
    ReviewResponseDto approveReview(@PathVariable Long reviewId);

    @PatchMapping("api/reviews/admin/{reviewId}/reject")
    ReviewResponseDto rejectReview(@PathVariable Long reviewId);

    @DeleteMapping("api/reviews/admin/{reviewId}")
    void deleteReview(@PathVariable Long reviewId);

    @GetMapping("/api/reviews/admin")
    Page<ReviewResponseDto> getPendingReviews(@RequestParam("page") int page,
                                              @RequestParam("size") int size,
                                              @RequestParam(value = "sort", required = false,defaultValue = "createdAt,desc") String sort);
}
