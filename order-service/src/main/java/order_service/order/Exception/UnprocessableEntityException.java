package order_service.order.Exception;

public class UnprocessableEntityException extends RuntimeException {
    public UnprocessableEntityException(String message,Throwable cause) {
        super(message,cause);
    }
}
