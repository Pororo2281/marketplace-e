package product_service.demo.Exception;

public class SlugExists extends RuntimeException {
    public SlugExists(String message) {
        super(message);
    }
}
