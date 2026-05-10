package com.marketplace.review_service.Controller;

import com.marketplace.review_service.Service.ReviewImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/reviews/images")
public class ReviewImageController {

    private final ReviewImageService reviewImageService;
    private static final Logger log = LoggerFactory.getLogger(ReviewImageController.class);

    public ReviewImageController(ReviewImageService reviewImageService) {
        this.reviewImageService = reviewImageService;
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<List<com.marketplace.review_service.dto.response.ReviewImageResponse>> getReviewImages(@PathVariable Long reviewId) {
        log.info("Fetching images for reviewId={}", reviewId);
        return ResponseEntity.ok(reviewImageService.getImagesByReviewId(reviewId));
    }

    @PostMapping(value = "/{reviewId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<com.marketplace.review_service.dto.response.ReviewImageResponse>> uploadReviewImage(@PathVariable Long reviewId,
                                                                                                                   @RequestParam ("images") List<MultipartFile> images) {
        log.info("Uploading {} images for reviewId={}", images.size(), reviewId);
        return ResponseEntity.ok(reviewImageService.addImageToReview(reviewId,images));
    }

    @DeleteMapping("/{id}/{reviewId}")
    public ResponseEntity<?> deleteReviewImage(@PathVariable Long id,
                                               @PathVariable Long reviewId) {

        log.info("Deleting id={} from reviewId={}", id, reviewId);
        reviewImageService.deleteReviewImage(id,reviewId);
        return ResponseEntity.noContent().build();
    }
}
