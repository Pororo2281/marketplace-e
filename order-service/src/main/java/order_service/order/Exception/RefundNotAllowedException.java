package order_service.order.Exception;

public class RefundNotAllowedException extends RuntimeException {
    public RefundNotAllowedException(String message) {
        super(message);
    }
}
