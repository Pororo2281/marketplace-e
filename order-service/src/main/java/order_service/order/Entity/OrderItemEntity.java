package order_service.order.Entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "order_items")
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "product_title", nullable = false)
    private String productTitle;

    @Column(name = "product_type")
    private String productType;

    @Column(name = "product_image_url")
    private String productImageUrl;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "price_at_purchase", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceAtPurchase;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "download_url")
    private String downloadUrl;

    @Column(name = "license_key")
    private String licenseKey;

    @Column(name = "access_granted_at")
    private Instant accessGrantedAt;

    @Column(name = "downloaded_at")
    private Instant downloadedAt;

    @Column(name = "download_count")
    private Integer downloadCount = 0;

    @Column(name = "max_downloads")
    private Integer maxDownloads = 5;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public OrderEntity getOrder() { return order; }
    public void setOrder(OrderEntity order) { this.order = order; }

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
