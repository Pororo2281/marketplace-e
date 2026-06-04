package payment_service.payment_service.Exception;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

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

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ExceptionResponse> handleInternalServerException(InternalServerException ex) {
        log.error("exception", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ExceptionResponse(
                        ex.getMessage(),
                        500,
                        Instant.now()
                )
        );
    }

    @ExceptionHandler({ConflictException.class,PaymentAlreadyRefundedException.class})
    public ResponseEntity<ExceptionResponse> handleConflictException(Exception ex){
        log.error("exception", ex);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ExceptionResponse(

                ex.getMessage(),
                409,
                Instant.now()
        ));
    }

    @ExceptionHandler({BadRequestException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ExceptionResponse> handleBadRequest(Exception ex){
        log.error("exception", ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionResponse(
                ex.getMessage(),
                400,
                Instant.now()
        ));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedException(UnauthorizedException ex){
        log.error("exception", ex);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ExceptionResponse(
                ex.getMessage(),
                401,
                Instant.now()
        ));
    }

    @ExceptionHandler({ForbiddenException.class})
    public ResponseEntity<ExceptionResponse> handleForbiddenException(Exception ex){
        log.error("exception", ex);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ExceptionResponse(
                ex.getMessage(),
                403,
                Instant.now()
        ));
    }

    @ExceptionHandler({ResourceNotFoundException.class,NotFoundById.class})
    public ResponseEntity<ExceptionResponse> handlerNotFoundById(NotFoundById ex) {
        log.error("exception", ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ExceptionResponse(
                        ex.getMessage(),
                        404,
                        Instant.now()
                )
        );
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<ExceptionResponse> handleUnprocessableEntityException(UnprocessableEntityException ex){
        log.error("exception", ex);

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(
                new ExceptionResponse(
                ex.getMessage(),
                422,
                Instant.now()
        ));
    }
}
