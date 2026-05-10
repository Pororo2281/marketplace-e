package order_service.order.Controller;

import jakarta.validation.Valid;
import order_service.order.Request.AddToCartRequest;
import order_service.order.Request.UpdateCartItemRequest;
import order_service.order.Request.UpdateOrderStatusRequest;
import order_service.order.Response.CartItemResponse;
import order_service.order.Response.CartResponse;
import order_service.order.Response.OrderDetailResponse;
import order_service.order.Service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(@RequestHeader("X-USER-ID") Long userId){
       return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/items")
    public ResponseEntity<CartItemResponse> addItem(@RequestHeader("X-USER-ID") Long userId,
                                                    @Valid @RequestBody AddToCartRequest request){
        return ResponseEntity.ok(cartService.addItem(userId,request));
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<CartItemResponse> updateStock( @PathVariable Long productId,
                                          @Valid @RequestBody UpdateCartItemRequest request,
                                          @RequestHeader("X-USER-ID") Long userId
                                         ){
        return ResponseEntity.ok(cartService.updateStock(productId,request,userId));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long productId,
                                           @RequestHeader("X-USER-ID") Long userId
                                           ){
        cartService.deleteItem(productId,userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> removeAllCart(@RequestHeader("X-USER-ID") Long userId){
        cartService.deleteAllCart(userId);
        return ResponseEntity.noContent().build();
    }

}
