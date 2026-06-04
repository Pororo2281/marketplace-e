package payment_service.payment_service.Exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message,Throwable cause) {
        super(message,cause);
    }
}
