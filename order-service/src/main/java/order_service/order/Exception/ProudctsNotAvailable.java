package order_service.order.Exception;

public class ProudctsNotAvailable extends RuntimeException {
    public ProudctsNotAvailable(String message) {
        super(message);
    }
}
