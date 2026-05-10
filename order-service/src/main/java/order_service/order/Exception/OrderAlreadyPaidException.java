package order_service.order.Exception;

public class OrderAlreadyPaidException extends RuntimeException {
    public OrderAlreadyPaidException(String message) {
        super(message);
    }
}
