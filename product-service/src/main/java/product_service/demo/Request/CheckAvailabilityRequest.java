package product_service.demo.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class CheckAvailabilityRequest {

    @NotEmpty(message = "Items list cannot be empty")
    @Valid
    private List<CheckAvailabilityItem> items;


    public CheckAvailabilityRequest() {}

    public CheckAvailabilityRequest(List<CheckAvailabilityItem> items) {
        this.items = items;
    }

    public List<CheckAvailabilityItem> getItems() { return items; }
    public void setItems(List<CheckAvailabilityItem> items) { this.items = items; }

}
