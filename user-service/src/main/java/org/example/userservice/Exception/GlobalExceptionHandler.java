package org.example.userservice.Exception;

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

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericException(Exception ex){
        log.error("exception", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(
                ex.getMessage(),
                500,
                Instant.now()
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handlerBadRequest(MethodArgumentNotValidException ex){
        log.error("exception", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(
                ex.getMessage(),
                400,
                Instant.now()
        ));
    }

    @ExceptionHandler({NotFoundById.class, UserNotFoundByEmail.class})
    public ResponseEntity<ExceptionResponse> handlerNotFoundById(Exception ex){
        log.error("exception", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(
                ex.getMessage(),
                404,
                Instant.now()
        ));
    }

    @ExceptionHandler({EmailAlreadyExistsException.class, SellerProfileExists.class})
    public ResponseEntity<ExceptionResponse> handleConflictException(Exception ex){
        log.error("exception", ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(
                ex.getMessage(),
                409,
                Instant.now()
        ));
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidRefreshToken(InvalidRefreshTokenException ex){
        log.error("exception", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionResponse(
                ex.getMessage(),
                401,
                Instant.now()
        ));
    }

}
