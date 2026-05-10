package product_service.demo.Response;

import java.util.List;

public class CheckAvailabilityResponse {

    private Boolean available;
    private String message;
    private List<ProductAvailabilityResponse> items;

    public CheckAvailabilityResponse() {}

    public CheckAvailabilityResponse(Boolean available, List<ProductAvailabilityResponse> items) {
        this.available = available;
        this.items = items;
    }

    public CheckAvailabilityResponse(Boolean available, List<ProductAvailabilityResponse> items, String message) {
        this.available = available;
        this.items = items;
        this.message = message;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ProductAvailabilityResponse> getItems() {
        return items;
    }

    public void setItems(List<ProductAvailabilityResponse> items) {
        this.items = items;
    }
}
