package product_service.demo.Exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericException(Exception ex){
        log.error("exception", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(
                ex.getMessage(),
                500,
                java.time.Instant.now()
        ));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ExceptionResponse> handleFileStorageException(FileStorageException ex){
        log.error("exception", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(
                ex.getMessage(),
                500,
                java.time.Instant.now()
        ));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({ProductArchivedException.class,ProductModificationNotAllowedException.class,SlugExists.class})
    public ResponseEntity<ExceptionResponse> handlerConflict(Exception ex){
        log.error("exception", ex);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(
                ex.getMessage(),
                409,
                java.time.Instant.now()
        ));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({NotFoundById.class, NotFoundBySlug.class})
    public ResponseEntity<ExceptionResponse> handlerNotFound(Exception ex){
        log.error("exception", ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(
                ex.getMessage(),
                404,
                java.time.Instant.now()
        ));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ProductImagesRequiredException.class)
    public ResponseEntity<ExceptionResponse> handleBadRequest(ProductImagesRequiredException ex){
        log.error("exception", ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(
                ex.getMessage(),
                400,
                java.time.Instant.now()
        ));
    }


}
