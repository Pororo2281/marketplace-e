package order_service.order.Exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message,Throwable cause) {
        super(message,cause);
    }
}
