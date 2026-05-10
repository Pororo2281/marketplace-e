package order_service.order.Controller;

import jakarta.validation.Valid;
import order_service.order.Enum.OrderStatus;
import order_service.order.Request.UpdateOrderStatusRequest;
import order_service.order.Response.OrderDetailResponse;
import order_service.order.Response.OrderResponse;
import order_service.order.Service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders/sellers")
public class SellerController {

    private final OrderService orderService;

    public SellerController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getSellerOrders(
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) OrderStatus status,
            @RequestHeader("X-USER-ID") Long sellerId){
        return ResponseEntity.ok(orderService.getSellersOrders(size,page,status,sellerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> getSellerOrder(
            @PathVariable Long id,
            @RequestHeader("X-USER-ID") Long sellerId){
        return ResponseEntity.ok(orderService.getSellerOrderById(id,sellerId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<OrderDetailResponse> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequest request,
            @RequestHeader("X-USER-ID") Long sellerId
    ) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id,request,sellerId));
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats(@RequestHeader("X-USER-ID") Long sellerId){
        return ResponseEntity.ok(orderService.getSellerStats(sellerId));
    }

}
