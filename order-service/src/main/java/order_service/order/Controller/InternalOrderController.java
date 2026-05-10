package order_service.order.Controller;

import order_service.order.Request.PaymentStatusRequest;
import order_service.order.Service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/internal/orders")
public class InternalOrderController {

    private final OrderService orderService;

    @Value("${internal.secret-token}")
    private String internalToken;

    public InternalOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/check-product/{productId}")
    public ResponseEntity<Boolean> checkProductInActiveOrders(@PathVariable Long productId) {
        boolean hasActiveOrders = orderService.hasActiveOrdersWithProduct(productId);
        return ResponseEntity.ok(hasActiveOrders);
    }

    @GetMapping("/check-purchase")
    public ResponseEntity<Boolean> checkUserPurchase(
            @RequestParam Long userId,
            @RequestParam Long productId
    ) {
        boolean purchased = orderService.hasUserPurchasedProduct(userId, productId);
        return ResponseEntity.ok(purchased);
    }
}

