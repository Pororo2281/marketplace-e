package payment_service.payment_service.Exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String message,Throwable cause) {
        super(message,cause);
    }
}
