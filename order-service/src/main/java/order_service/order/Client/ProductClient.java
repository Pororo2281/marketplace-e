package order_service.order.Client;

import jakarta.validation.Valid;
import order_service.order.Request.CheckAvailabilityRequest;
import order_service.order.Response.CheckAvailabilityResponse;
import order_service.order.Response.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "product-service",url = "http://localhost:8082")
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    ProductResponse getProductById(@PathVariable Long id);

    @PostMapping("/api/internal/products/check-availability")
    CheckAvailabilityResponse checkProductAvailability(@RequestBody CheckAvailabilityRequest request,
                                                       @RequestHeader("X-Api-Key") String apiKey);

    @PostMapping("api/internal/products/reduce-stock")
    void reverseStock(@RequestBody CheckAvailabilityRequest availabilityRequest,
                      @RequestHeader("X-Api-Key") String apiKey);

    @PostMapping("api/internal/products/return-stock")
    void returnStock(@Valid @RequestBody CheckAvailabilityRequest request,
                     @RequestHeader("X-Api-Key") String apiKey);


}
