package product_service.demo.Event;

import product_service.demo.Enum.ProductEventType;

import java.util.List;

public class ProductBatchEvent {
    private ProductEventType type;
    private List<ProductEvent> events;

    public ProductEventType getType() {
        return type;
    }

    public void setType(ProductEventType type) {
        this.type = type;
    }

    public List<ProductEvent> getEvents() {
        return events;
    }

    public void setEvents(List<ProductEvent> events) {
        this.events = events;
    }
}
