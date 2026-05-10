package order_service.order.Controller;

import order_service.order.Response.PurchasedProductResponse;
import order_service.order.Service.LibraryService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/library")
public class LibraryController {

    private final LibraryService service;

    public LibraryController(LibraryService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<PurchasedProductResponse>> getMyLibrary(
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "0") int page,
            @RequestHeader("X-USER-ID") Long userId
    ){
        return ResponseEntity.ok(service.getMyLibrary(size,page,userId));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<PurchasedProductResponse> getPurchasedProduct(@PathVariable Long productId,
                                                                        @RequestHeader("X-USER-ID") Long userId){
        return ResponseEntity.ok(service.getProduct(productId,userId));
    }

    @GetMapping("/check/{productId}")
    public ResponseEntity<Boolean> checkPurchased(
            @PathVariable Long productId,
            @RequestHeader("X-USER-ID") Long userId
    ) {
        return ResponseEntity.ok(service.purchased(productId,userId));
    }
}
