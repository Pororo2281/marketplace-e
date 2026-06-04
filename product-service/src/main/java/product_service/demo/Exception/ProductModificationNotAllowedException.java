package product_service.demo.Exception;

public class ProductModificationNotAllowedException extends RuntimeException {
    public ProductModificationNotAllowedException(String message) {
        super(message);
    }
}
