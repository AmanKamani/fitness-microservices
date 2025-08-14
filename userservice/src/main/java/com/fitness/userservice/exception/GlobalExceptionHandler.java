package com.fitness.userservice.exception;

import com.fitness.userservice.dto.ApiErrorResponse;
import com.fitness.userservice.util.ApplicationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiErrorResponse> handleBaseException(BaseException ex) {
        return ResponseEntity
                .status(ex.getApiError().httpStatus())
                .body(new ApiErrorResponse(ex.getApiError()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

        return ResponseEntity
                .status(ApplicationError.INVALID_REQUEST_BODY.httpStatus())
                .body(new ApiErrorResponse(ApplicationError.INVALID_REQUEST_BODY, errors));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.error("Invalid argument type", ex);
        ApiErrorResponse response = new ApiErrorResponse(ApplicationError.INVALID_REQUEST_BODY);
        response.setMessage(ex.getMessage());

        return ResponseEntity
                .status(ApplicationError.INVALID_REQUEST_BODY.httpStatus())
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception ex) {
        log.error("Unknown Error", ex);

        ApiErrorResponse response = new ApiErrorResponse(ApplicationError.UNKNOWN_ERROR);
        response.setMessage(ex.getMessage());

        return ResponseEntity
                .status(ApplicationError.UNKNOWN_ERROR.httpStatus())
                .body(response);
    }
}
