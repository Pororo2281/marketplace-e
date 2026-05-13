package com.marketplace.review_service.Entity;

import com.marketplace.review_service.Enum.ReviewStatus;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reviews", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "product_id"})
})
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "seller_id", nullable = false)
    private Long sellerId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "product_title")
    private String productTitle;

    @Column(name = "user_avatar_url")
    private String userAvatarUrl;

    @Column(nullable = false)
    private Integer rating;

    @Column(length = 200)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(columnDefinition = "TEXT")
    private String pros;

    @Column(columnDefinition = "TEXT")
    private String cons;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImageEntity> images = new ArrayList<>();

    @Column(name = "helpful_count", nullable = false)
    private Integer helpfulCount = 0;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewHelpfulEntity> helpfulVotes = new ArrayList<>();


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewStatus status = ReviewStatus.PENDING; // PENDING, APPROVED, REJECTED

    @Column(name = "moderation_reason")
    private String moderationReason;

    @Column(name = "moderated_at")
    private Instant moderatedAt;

    @Column(name = "moderated_by")
    private Long moderatedBy; // ID админа

    @OneToOne(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private SellerResponseEntity sellerResponse;


    @Column(name = "verified_purchase", nullable = false)
    private Boolean verifiedPurchase = true;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = Instant.now();
    }

    public void addImage(ReviewImageEntity image) {
        images.add(image);
        image.setReview(this);
    }

    public void removeImage(ReviewImageEntity image) {
        images.remove(image);
        image.setReview(null);
    }

    public void incrementHelpfulCount() {
        this.helpfulCount++;
    }

    public void decrementHelpfulCount() {
        if (this.helpfulCount > 0) {
            this.helpfulCount--;
        }
    }


    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPros(String pros) {
        this.pros = pros;
    }

    public void setCons(String cons) {
        this.cons = cons;
    }

    public void setImages(List<ReviewImageEntity> images) {
        this.images = images;
    }

    public void setHelpfulCount(Integer helpfulCount) {
        this.helpfulCount = helpfulCount;
    }

    public void setHelpfulVotes(List<ReviewHelpfulEntity> helpfulVotes) {
        this.helpfulVotes = helpfulVotes;
    }

    public void setStatus(ReviewStatus status) {
        this.status = status;
    }

    public void setModerationReason(String moderationReason) {
        this.moderationReason = moderationReason;
    }

    public void setModeratedAt(Instant moderatedAt) {
        this.moderatedAt = moderatedAt;
    }

    public void setModeratedBy(Long moderatedBy) {
        this.moderatedBy = moderatedBy;
    }

    public void setSellerResponse(SellerResponseEntity sellerResponse) {
        this.sellerResponse = sellerResponse;
    }

    public void setVerifiedPurchase(Boolean verifiedPurchase) {
        this.verifiedPurchase = verifiedPurchase;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public String getUserName() {
        return userName;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public Integer getRating() {
        return rating;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getPros() {
        return pros;
    }

    public String getCons() {
        return cons;
    }

    public List<ReviewImageEntity> getImages() {
        return images;
    }

    public Integer getHelpfulCount() {
        return helpfulCount;
    }

    public List<ReviewHelpfulEntity> getHelpfulVotes() {
        return helpfulVotes;
    }

    public ReviewStatus getStatus() {
        return status;
    }

    public String getModerationReason() {
        return moderationReason;
    }

    public Instant getModeratedAt() {
        return moderatedAt;
    }

    public Long getModeratedBy() {
        return moderatedBy;
    }

    public SellerResponseEntity getSellerResponse() {
        return sellerResponse;
    }

    public Boolean getVerifiedPurchase() {
        return verifiedPurchase;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
