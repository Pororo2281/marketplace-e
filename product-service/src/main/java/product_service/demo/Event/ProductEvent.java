package product_service.demo.Event;

import product_service.demo.Enum.ProductEventType;

import java.math.BigDecimal;

public class ProductEvent {
    private String id;
    private String title;
    private String description;
    private BigDecimal price;
    private String category;
    private String seller;
    private ProductEventType type;

    public ProductEventType getType() {
        return type;
    }

    public void setType(ProductEventType type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }
}
