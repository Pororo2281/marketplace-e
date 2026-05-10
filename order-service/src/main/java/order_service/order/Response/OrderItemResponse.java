package order_service.order.Response;

import java.math.BigDecimal;
import java.time.Instant;

public class OrderItemResponse {

    private Long id;
    private Long productId;
    private String productTitle;
    private String productType;
    private String productImageUrl;
    private Integer quantity;
    private BigDecimal priceAtPurchase;
    private BigDecimal subtotal;

    // Для цифровых товаров
    private String downloadUrl;
    private String licenseKey;
    private Instant accessGrantedAt;
    private Instant downloadedAt;
    private Integer downloadCount;
    private Integer maxDownloads;

    public OrderItemResponse() {}

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

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getPriceAtPurchase() { return priceAtPurchase; }
    public void setPriceAtPurchase(BigDecimal priceAtPurchase) { this.priceAtPurchase = priceAtPurchase; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public String getDownloadUrl() { return downloadUrl; }
    public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }

    public String getLicenseKey() { return licenseKey; }
    public void setLicenseKey(String licenseKey) { this.licenseKey = licenseKey; }

    public Instant getAccessGrantedAt() { return accessGrantedAt; }
    public void setAccessGrantedAt(Instant accessGrantedAt) { this.accessGrantedAt = accessGrantedAt; }

    public Instant getDownloadedAt() { return downloadedAt; }
    public void setDownloadedAt(Instant downloadedAt) { this.downloadedAt = downloadedAt; }

    public Integer getDownloadCount() { return downloadCount; }
    public void setDownloadCount(Integer downloadCount) { this.downloadCount = downloadCount; }

    public Integer getMaxDownloads() { return maxDownloads; }
    public void setMaxDownloads(Integer maxDownloads) { this.maxDownloads = maxDownloads; }
}