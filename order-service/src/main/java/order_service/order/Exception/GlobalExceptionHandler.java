package order_service.order.Exception;

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

    @org.springframework.web.bind.annotation.ExceptionHandler(ProductServiceException.class)
    public ResponseEntity<ExceptionResponse> handlerServiceUnavailable(ProductServiceException ex){
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ExceptionResponse(
                ex.getMessage(),
                503,
                java.time.Instant.now()
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
                    ,RefundNotAllowedException.class})
    public ResponseEntity<ExceptionResponse> handlerConflict(Exception ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(
                ex.getMessage(),
                409,
                java.time.Instant.now()
        ));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({NotFoundById.class, NotFoundByOrderNumber.class, CartItemOwnerException.class})
    public ResponseEntity<ExceptionResponse> handlerNotFound(Exception ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(
                ex.getMessage(),
                404,
                java.time.Instant.now()
        ));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(EmptyCartException.class)
    public ResponseEntity<ExceptionResponse> handlerBadRequest(EmptyCartException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(
                ex.getMessage(),
                400,
                java.time.Instant.now()
        ));
    }

}
