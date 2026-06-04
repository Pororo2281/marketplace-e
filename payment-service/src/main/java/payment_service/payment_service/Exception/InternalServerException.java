package payment_service.payment_service.Exception;

public class InternalServerException extends RuntimeException {
    public InternalServerException(String message,Throwable cause) {
        super(message,cause);
    }
}
