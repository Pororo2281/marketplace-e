package product_service.demo.Exception;

public class ProductImagesRequiredException extends RuntimeException {
    public ProductImagesRequiredException(String message) {
        super(message);
    }
}
