package order_service.order.Exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message,Throwable cause) {
        super(message,cause);
    }
}
