package app.exceptions;

import app.dto.main.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleContentBadRequest(BadRequestException e) {
        return new ResponseEntity<>(
                new ErrorResponse(
                        e.getType().getText(),
                        e.getDescription()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ErrorResponse> handleContentUnauthorized(UnAuthorizedException e) {
        return new ResponseEntity<>(
                new ErrorResponse(
                        e.getType().getText(),
                        e.getDescription()
                ),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<ErrorResponse> handleContentServiceUnavailable(ServiceUnavailableException e) {
        return new ResponseEntity<>(
                new ErrorResponse(
                        e.getType().getText(),
                        e.getDescription()
                ),
                HttpStatus.SERVICE_UNAVAILABLE);
    }

}