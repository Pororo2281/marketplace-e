package order_service.order.Exception;

public class ProductsNotEnough extends RuntimeException {
    public ProductsNotEnough(String message) {
        super(message);
    }
}
