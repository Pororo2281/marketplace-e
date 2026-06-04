package payment_service.payment_service.Exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message,Throwable cause) {
        super(message,cause);
    }
}
