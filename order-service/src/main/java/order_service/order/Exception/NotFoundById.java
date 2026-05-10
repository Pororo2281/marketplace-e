package order_service.order.Exception;

public class NotFoundById extends RuntimeException {
    public NotFoundById(String message) {
        super(message);
    }
}
