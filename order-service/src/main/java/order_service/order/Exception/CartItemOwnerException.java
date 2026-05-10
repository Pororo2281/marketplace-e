package order_service.order.Exception;

public class CartItemOwnerException extends RuntimeException {
    public CartItemOwnerException(String message) {
        super(message);
    }
}
