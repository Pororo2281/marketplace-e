package order_service.order.Exception;

public class NotFoundByOrderNumber extends RuntimeException {
    public NotFoundByOrderNumber(String message) {
        super(message);
    }
}
