package payment_service.payment_service.Exception;

public class UnprocessableEntityException extends RuntimeException {
    public UnprocessableEntityException(String message,Throwable cause) {
        super(message,cause);
    }
}
