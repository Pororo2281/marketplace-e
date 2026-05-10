package order_service.order.Exception;

public class ProductOutOfStockException extends RuntimeException {
    public ProductOutOfStockException(String message) {
        super(message);
    }
}
