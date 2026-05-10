package order_service.order.Response;

import java.time.Instant;

public class PurchasedProductResponse {

    private Long id;
    private Long productId;
    private String productTitle;
    private String productType;
    private String productImageUrl;

    // Информация о заказе
    private Long orderId;
    private String orderNumber;

    // Доступ к товару
    private String downloadUrl;
    private String licenseKey;

    // Даты
    private Instant purchasedAt;
    private Instant lastAccessedAt;
    private Integer accessCount;

    public PurchasedProductResponse() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductTitle() { return productTitle; }
    public void setProductTitle(String productTitle) { this.productTitle = productTitle; }

    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }

    public String getProductImageUrl() { return productImageUrl; }
    public void setProductImageUrl(String productImageUrl) { this.productImageUrl = productImageUrl; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

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