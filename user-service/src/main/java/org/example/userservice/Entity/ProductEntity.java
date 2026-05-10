package org.example.userservice.Entity;

import org.example.userservice.Enums.ProductStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seller_profile_id", nullable = false)
    private SellerProfileEntity sellerProfile;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    private String title;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "stock_quantity",nullable = false)
    private Integer stockQuantity;

    @Column(name = "image_urls")
    private List<String> imageUrls = new ArrayList<>();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewEntity> reviews = new ArrayList<>();

    public ProductEntity() {
    }

    public ProductEntity(SellerProfileEntity sellerProfile, CategoryEntity category, BigDecimal price, Integer stockQuantity,Instant createdAt) {
        this.sellerProfile = sellerProfile;
        this.category = category;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SellerProfileEntity getSellerProfile() {
        return sellerProfile;
    }

    public void setSellerProfile(SellerProfileEntity sellerProfile) {
        this.sellerProfile = sellerProfile;
    }

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<ReviewEntity> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewEntity> reviews) {
        this.reviews = reviews;
    }
}
