package search.search.Exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericException(Exception ex) {
        log.error("exception", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ExceptionResponse(
                        ex.getMessage(),
                        500,
                        Instant.now()
                )
        );
    }

    @ExceptionHandler(SearchServiceException.class)
    public ResponseEntity<ExceptionResponse> handleSearchServiceException(SearchServiceException ex) {
        log.error("exception", ex);

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(
                new ExceptionResponse(
                        ex.getMessage(),
                        503,
                        Instant.now()
                )
        );
    }


}
