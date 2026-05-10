package org.example.userservice.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericException(Exception ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(
                ex.getMessage(),
                500,
                Instant.now()
        ));
    }

    @ExceptionHandler(NotFoundById.class)
    public ResponseEntity<ExceptionResponse> handlerNotFoundById(NotFoundById ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(
                ex.getMessage(),
                404,
                Instant.now()
        ));
    }

    @ExceptionHandler({EmailAlreadyExistsException.class, SellerProfileExists.class})
    public ResponseEntity<ExceptionResponse> handleConflictException(Exception ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(
                ex.getMessage(),
                409,
                Instant.now()
        ));
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidRefreshToken(InvalidRefreshTokenException ex){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionResponse(
                ex.getMessage(),
                401,
                Instant.now()
        ));
    }

}
