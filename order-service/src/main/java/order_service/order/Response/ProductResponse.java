package order_service.order.Response;

import java.math.BigDecimal;

public class ProductResponse {

    private Long id;
    private String title;
    private BigDecimal price;
    private String mainImageUrl;
    private String categoryName;
    private Integer stockQuantity;


    private Long sellerId;
    private String sellerName;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }


    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
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

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }



    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }
}
