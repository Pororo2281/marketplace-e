package order_service.order.Exception;

import org.apache.coyote.BadRequestException;
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

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericException(Exception ex){
        log.error("exception", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(
                ex.getMessage(),
                500,
                java.time.Instant.now()
        ));
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

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedException(UnauthorizedException ex){
        log.error("exception", ex);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionResponse(
                ex.getMessage(),
                401,
                Instant.now()
        ));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ProductServiceException.class)
    public ResponseEntity<ExceptionResponse> handlerServiceUnavailable(ProductServiceException ex){
        log.error("exception", ex);

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ExceptionResponse(
                ex.getMessage(),
                503,
                java.time.Instant.now()
        ));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ExceptionResponse> handleForbiddenException(ForbiddenException ex){
        log.error("exception", ex);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionResponse(
                ex.getMessage(),
                403,
                Instant.now()
        ));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(
            {InsufficientStockException.class
                    ,InvalidOrderStateException.class
                    ,OrderAlreadyPaidException.class
                    ,OrderUpdateNotAllowedException.class
                    ,ProductOutOfStockException.class
                    ,ProductsNotEnough.class
                    ,ProudctsNotAvailable.class
                    ,RefundNotAllowedException.class,
                    ConflictException.class})
    public ResponseEntity<ExceptionResponse> handlerConflict(Exception ex){
        log.error("exception", ex);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(
                ex.getMessage(),
                409,
                java.time.Instant.now()
        ));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({NotFoundById.class, NotFoundByOrderNumber.class, CartItemOwnerException.class,ResourceNotFoundException.class})
    public ResponseEntity<ExceptionResponse> handlerNotFound(Exception ex){
        log.error("exception", ex);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(
                ex.getMessage(),
                404,
                java.time.Instant.now()
        ));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({EmptyCartException.class, BadRequestException.class})
    public ResponseEntity<ExceptionResponse> handlerBadRequest(Exception ex){
        log.error("exception", ex);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(
                ex.getMessage(),
                400,
                java.time.Instant.now()
        ));
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    public ResponseEntity<ExceptionResponse> handleUnprocessableEntityException(UnprocessableEntityException ex){
        log.error("exception", ex);

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT).body(new ExceptionResponse(


                ex.getMessage(),
                422,
                Instant.now()
        ));
    }

}
