package order_service.order.Exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message,Throwable cause) {
        super(message,cause);
    }
}
