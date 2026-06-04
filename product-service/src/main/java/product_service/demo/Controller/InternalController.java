package product_service.demo.Controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import product_service.demo.Request.CheckAvailabilityRequest;
import product_service.demo.Response.CheckAvailabilityResponse;
import product_service.demo.Response.ProductResponse;
import product_service.demo.Service.InternalProductService;

import java.util.List;

@RestController
@RequestMapping("/api/internal/products")
public class InternalController {

    private final InternalProductService service;

    @Value("${internal.secret-token}")
    private String internalToken;
    public InternalController(InternalProductService service) {
        this.service = service;
    }

    @PostMapping("/check-availability")
    public ResponseEntity<CheckAvailabilityResponse> checkProductAvailability(@Valid @RequestBody CheckAvailabilityRequest request,
                                                                              @RequestHeader("X-Api-Key") String apiKey){
        if (!internalToken.equals(apiKey)) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(service.checkAvailability(request));
    }

    @PostMapping("/reserve-stock")
    public ResponseEntity<Void> reverseStock(@Valid @RequestBody CheckAvailabilityRequest request,
                                             @RequestHeader("X-Api-Key") String apiKey){
        if (!internalToken.equals(apiKey)) return ResponseEntity.status(401).build();
        boolean reserved = service.reverse(request);
        if (!reserved){
            return ResponseEntity.status(409).build();
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/reduce-stock")
    public ResponseEntity<Void> reduceStock(@Valid @RequestBody CheckAvailabilityRequest request,
                                            @RequestHeader("X-Api-Key") String apiKey) {
        if (!internalToken.equals(apiKey)) return ResponseEntity.status(401).build();
        service.reduceStock(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/return-stock")
    public ResponseEntity<Void> returnStock(@Valid @RequestBody CheckAvailabilityRequest request,
                                            @RequestHeader("X-Api-Key") String apiKey) {
        if (!internalToken.equals(apiKey)) return ResponseEntity.status(401).build();
        service.returnStock(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/batch")
    public ResponseEntity<List<ProductResponse>> getProductsBatch(@RequestParam List<Long> ids,
                                                                  @RequestHeader("X-Api-Key") String apiKey){
        if (!internalToken.equals(apiKey)) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(service.getProductsBatch(ids));
    }

    @GetMapping()
    public ResponseEntity<ProductResponse> getProduct(@RequestParam Long productId){
        return ResponseEntity.ok(service.getSellerIdByProductId(productId));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Void> approveProduct(@PathVariable Long id){
        service.approveProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Void> rejectProduct(@PathVariable Long id){
        service.rejectProduct(id);
        return ResponseEntity.noContent().build();
    }
}
