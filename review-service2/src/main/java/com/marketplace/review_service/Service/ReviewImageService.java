package com.marketplace.review_service.Service;

import com.marketplace.review_service.Entity.ReviewEntity;
import com.marketplace.review_service.Entity.ReviewImageEntity;
import com.marketplace.review_service.Exception.NotFoundById;
import com.marketplace.review_service.Mapper.ReviewImageMapper;
import com.marketplace.review_service.Repository.ReviewImageRepo;
import com.marketplace.review_service.Repository.ReviewRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewImageService {

    private final ReviewImageRepo reviewImageRepo;
    private final ReviewRepo reviewRepo;
    private final MinIoService minIoService;

    @Value("${minio.bucket}")
    private String bucketName;

    public ReviewImageService(ReviewImageRepo reviewImageRepo, ReviewRepo reviewRepo, MinIoService minIoService) {
        this.reviewImageRepo = reviewImageRepo;
        this.reviewRepo = reviewRepo;
        this.minIoService = minIoService;
    }

    @Transactional
    public List<com.marketplace.review_service.dto.response.ReviewImageResponse> addImageToReview(Long id, List<MultipartFile> images) {

        var review = reviewRepo.findById(id)
                .orElseThrow(() -> new IllegalStateException("Review not found"));
        if (review.getImages().size() >= 5){
            throw new IllegalStateException("Maximum number of images for a review is 5");
        }
        int availableSlot = 5 - review.getImages().size();
        if (images.size()>availableSlot){
            throw new IllegalStateException("You can upload only " + availableSlot + " images for this review");
        }
        Long productId = review.getProductId();
        List<ReviewImageEntity> reviewImages = saveReviewImages(images, review, productId);
        review.getImages().addAll(reviewImages);
        reviewRepo.save(review);
        return  reviewImages.stream()
                .map(ReviewImageMapper::toResponse)
                .toList();
    }

    private List<ReviewImageEntity> saveReviewImages(List<MultipartFile> images, ReviewEntity review,Long productId) {
        List<ReviewImageEntity> reviewImages = new ArrayList<>();
        if (images == null || images.isEmpty()) {
            return List.of();
        }

        int sizeImages = review.getImages().size();

        for (int i = 0; i < images.size(); i++) {

            MultipartFile imageFile = images.get(i);

            String imageUrl = minIoService.uploadReviewFile(
                    imageFile,  productId.toString() + "/reviews"
            );

            ReviewImageEntity imageEntity = new ReviewImageEntity();
            imageEntity.setImageUrl(imageUrl);
            imageEntity.setReview(review);
            reviewImages.add(imageEntity);
            imageEntity.setDisplayOrder(sizeImages+i);


        }

        return reviewImages;
    }

    @Transactional
    public void deleteReviewImage(Long imageId,Long id) {

        var image = reviewImageRepo.findById(imageId)
                .orElseThrow(() -> new NotFoundById("Review image not found"));
        minIoService.deleteFile(image.getImageUrl(),bucketName);
        reviewImageRepo.delete(image);
        List<ReviewImageEntity> images = reviewImageRepo.findByReviewIdOrderByDisplayOrder(id);

        for (int i = 0; i < images.size(); i++) {
            images.get(i).setDisplayOrder(i);
        }
        reviewImageRepo.saveAll(images);
    }

    public List<com.marketplace.review_service.dto.response.ReviewImageResponse> getImagesByReviewId(Long id) {
        var review = reviewRepo.findById(id)
                .orElseThrow(() -> new NotFoundById("Review not found by id: " + id));
        return review.getImages().stream()
                .map(ReviewImageMapper::toResponse)
                .toList();
    }
}
