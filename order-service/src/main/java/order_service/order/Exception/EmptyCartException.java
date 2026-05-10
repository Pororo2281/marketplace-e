package order_service.order.Exception;

public class EmptyCartException extends RuntimeException {
    public EmptyCartException(String message) {
        super(message);
    }
}
