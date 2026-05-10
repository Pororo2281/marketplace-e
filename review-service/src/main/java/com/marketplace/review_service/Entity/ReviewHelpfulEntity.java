package com.marketplace.review_service.Entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "review_helpful", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"review_id", "user_id"})
})
public class ReviewHelpfulEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    private ReviewEntity review;

    @Column(name = "user_id", nullable = false)
    private Long userId; // Кто отметил полезным

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReviewEntity getReview() {
        return review;
    }

    public void setReview(ReviewEntity review) {
        this.review = review;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}