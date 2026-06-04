package order_service.order.Exception;

public class InternalServerException extends RuntimeException {
    public InternalServerException(String message,Throwable cause) {
        super(message,cause);
    }
}
