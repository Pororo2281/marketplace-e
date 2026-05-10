package order_service.order.Exception;

public class OrderUpdateNotAllowedException extends RuntimeException {
    public OrderUpdateNotAllowedException(String message) {
        super(message);
    }
}
