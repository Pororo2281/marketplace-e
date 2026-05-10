package product_service.demo.Exception;

public class NotFoundBySlug extends RuntimeException {
    public NotFoundBySlug(String message) {
        super(message);
    }
}
