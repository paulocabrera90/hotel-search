package com.pcabrera.hotel_search.infrastructure.config;

import com.pcabrera.hotel_search.infrastructure.config.exception.ApiException;
import com.pcabrera.hotel_search.infrastructure.config.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(
            ApiException ex,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = ex.getErrorCode();
        HttpStatus status = mapToHttpStatus(errorCode);

        ErrorResponse response = new ErrorResponse(
                errorCode.value(),
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(status)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        String message = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Validation error");

        ErrorResponse response = new ErrorResponse(
                ErrorCode.CLIENT_BAD_REQUEST.value(),
                message,
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        ErrorResponse response = new ErrorResponse(
                ErrorCode.CLIENT_BAD_REQUEST.value(),
                "Request body inválido. Verificá el formato JSON y las fechas (yyyy-MM-dd).",
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex,
            HttpServletRequest request
    ) {
        ErrorResponse response = new ErrorResponse(
                ErrorCode.INTERNAL_ERROR.value(),
                ErrorCode.INTERNAL_ERROR.getReasonPhrase(),
                request.getRequestURI(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    private HttpStatus mapToHttpStatus(ErrorCode errorCode) {
        if (errorCode == ErrorCode.INVALID_DATE_RANGE ||
                errorCode == ErrorCode.INVALID_SEARCH_ID ||
                errorCode == ErrorCode.CLIENT_BAD_REQUEST) {
            return HttpStatus.BAD_REQUEST;
        }

        if (errorCode == ErrorCode.SEARCH_NOT_FOUND) {
            return HttpStatus.NOT_FOUND;
        }

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}