package order_service.order.Controller;

import jakarta.validation.Valid;
import order_service.order.Enum.OrderStatus;
import order_service.order.Request.CreateOrderRequest;
import order_service.order.Request.UpdateOrderStatusRequest;
import order_service.order.Response.OrderDetailResponse;
import order_service.order.Response.OrderItemResponse;
import order_service.order.Response.OrderResponse;
import order_service.order.Service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<List<OrderDetailResponse>> createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            @RequestHeader("X-USER-ID") Long userId
            ){
        return ResponseEntity.ok(orderService.createOrder(request,userId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrder(
            @PathVariable Long orderId,
            @RequestHeader("X-USER-ID") Long userId){
        return ResponseEntity.ok(orderService.getOrderById(orderId,userId));
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getMyOrders(
            @RequestHeader("X-USER-ID") Long userId,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) OrderStatus status
            ){
        return ResponseEntity.ok(orderService.getMyOrders(userId,size,page,status));
    }

    @GetMapping("/number/{orderNumber}")
    public ResponseEntity<OrderResponse> getOrderByNumber(@RequestHeader("X-USER-ID") Long userId,
                                                     @PathVariable String orderNumber){
        return ResponseEntity.ok(orderService.getOrderByNumber(userId,orderNumber));
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderDetailResponse> cancelOrder(@RequestHeader("X-USER-ID") Long userId,
                                                     @PathVariable Long orderId,
                                                     @RequestBody(required = false) UpdateOrderStatusRequest request){
        return ResponseEntity.ok(orderService.cancelOrder(userId,orderId,request));
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<OrderDetailResponse> requestRefund(@PathVariable Long id,
                                                             @RequestBody(required = false) UpdateOrderStatusRequest request,
                                                             @RequestHeader("X-USER-ID") Long userId){
        return ResponseEntity.ok(orderService.requestRefund(id,request,userId));
    }

    @PostMapping("/{id}")
    public ResponseEntity<OrderDetailResponse> requestRefund(@PathVariable Long id){
        return ResponseEntity.ok(orderService.approveRefund(id));
    }

}
