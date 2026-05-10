package org.example.userservice.Response;

import java.time.Instant;

public class SellerResponse {
    private String companyName;
    private String description;
    private Instant createdAt;
    private Integer totalSales;
    private Double rating;

    public Integer getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(Integer totalSales) {
        this.totalSales = totalSales;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
