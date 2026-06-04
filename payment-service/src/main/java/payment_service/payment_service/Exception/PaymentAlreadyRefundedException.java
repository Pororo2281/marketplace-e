package payment_service.payment_service.Exception;

public class PaymentAlreadyRefundedException extends RuntimeException {
    public PaymentAlreadyRefundedException(String message) {
        super(message);
    }
}
