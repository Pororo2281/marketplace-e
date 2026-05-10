package org.example.userservice.Entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "seller_profiles")
public class SellerProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "company_name")
    private String companyName;

    private String description;

    private Double rating;

    @Column(name = "total_sales")
    private Integer totalSales;

    @Column(name = "created_at")
    private Instant createdAt;

    @OneToMany(
            mappedBy = "sellerProfile",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ProductEntity> products = new ArrayList<>();

    public SellerProfileEntity(UserEntity user, Instant createdAt, Integer totalSales) {
        this.user = user;
        this.createdAt = createdAt;
        this.totalSales = totalSales;
    }

    public SellerProfileEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(Integer totalSales) {
        this.totalSales = totalSales;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public List<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }
}
