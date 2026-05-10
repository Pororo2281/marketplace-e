package order_service.order.Response;
import java.math.BigDecimal;
import java.time.Instant;

public class CartItemResponse {

    private Long id;
    private Long productId;
    private String productTitle;
    private BigDecimal productPrice;
    private String mainImageUrl;
    private String productType;
    private Integer quantity;
    private BigDecimal subtotal;
    private Long sellerId;
    private String sellerName;
    private Instant addedAt;

    public CartItemResponse() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductTitle() { return productTitle; }
    public void setProductTitle(String productTitle) { this.productTitle = productTitle; }

    public BigDecimal getProductPrice() { return productPrice; }
    public void setProductPrice(BigDecimal productPrice) { this.productPrice = productPrice; }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }

    public String getProductType() { return productType; }
    public void setProductType(String productType) { this.productType = productType; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public Long getSellerId() { return sellerId; }
    public void setSellerId(Long sellerId) { this.sellerId = sellerId; }

    public String getSellerName() { return sellerName; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }

    public Instant getAddedAt() { return addedAt; }
    public void setAddedAt(Instant addedAt) { this.addedAt = addedAt; }
}
