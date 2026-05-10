package product_service.demo.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericException(Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(
                ex.getMessage(),
                500,
                java.time.Instant.now()
        ));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundById.class)
    public ResponseEntity<ExceptionResponse> handlerNotFoundById(NotFoundById ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(
                ex.getMessage(),
                404,
                java.time.Instant.now()
        ));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundBySlug.class)
    public ResponseEntity<ExceptionResponse> handlerNotFoundBySlug(NotFoundBySlug ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(
                    ex.getMessage(),
                    404,
                    java.time.Instant.now()
            ));
        }
}
