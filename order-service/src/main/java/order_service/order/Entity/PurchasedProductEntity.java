package order_service.order.Entity;

import jakarta.persistence.Entity;


import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "purchased_products")
public class PurchasedProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "order_item_id", nullable = false)
    private Long orderItemId;

    @Column(name = "product_title", nullable = false)
    private String productTitle;

    @Column(name = "product_type")
    private String productType;

    @Column(name = "product_image_url")
    private String productImageUrl;

    @Column(name = "download_url")
    private String downloadUrl;

    @Column(name = "license_key")
    private String licenseKey;

    @Column(name = "purchased_at", nullable = false)
    private Instant purchasedAt;

    @Column(name = "last_accessed_at")
    private Instant lastAccessedAt;

    @Column(name = "access_count")
    private Integer accessCount = 0;

    @PrePersist
    protected void onCreate() {
        this.purchasedAt = Instant.now();
        this.lastAccessedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastAccessedAt = Instant.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getOrderItemId() { return orderItemId; }
    public void setOrderItemId(Long orderItemId) { this.orderItemId = orderItemId; }

    public String getProductTitle() { return productTitle; }
    public void setProductTitle(String productTitle) { this.productTitle = productTitle; }

    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }

    public String getProductImageUrl() { return productImageUrl; }
    public void setProductImageUrl(String productImageUrl) { this.productImageUrl = productImageUrl; }

    public String getDownloadUrl() { return downloadUrl; }
    public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }

    public String getLicenseKey() { return licenseKey; }
    public void setLicenseKey(String licenseKey) { this.licenseKey = licenseKey; }

    public Instant getPurchasedAt() { return purchasedAt; }
    public void setPurchasedAt(Instant purchasedAt) { this.purchasedAt = purchasedAt; }

    public Instant getLastAccessedAt() { return lastAccessedAt; }
    public void setLastAccessedAt(Instant lastAccessedAt) { this.lastAccessedAt = lastAccessedAt; }

    public Integer getAccessCount() { return accessCount; }
    public void setAccessCount(Integer accessCount) { this.accessCount = accessCount; }
}
