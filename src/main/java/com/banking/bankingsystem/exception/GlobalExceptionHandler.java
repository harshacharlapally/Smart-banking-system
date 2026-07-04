package com.banking.bankingsystem.exception;

import com.banking.bankingsystem.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse>
            handleAccountNotFound(AccountNotFoundException ex,
                                  HttpServletRequest request){
        ErrorResponse error = new ErrorResponse(
                404,
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorResponse>
            handleInsufficinetBalance(InsufficientBalanceException ex,
                                      HttpServletRequest request){
        ErrorResponse error = new ErrorResponse(
                400,
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(DuplicateAccountException.class)
    public ResponseEntity<ErrorResponse>
            handeleDuplicateAccount(DuplicateAccountException ex, HttpServletRequest request){
        ErrorResponse error = new ErrorResponse(
                409,
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error);
    }

    @ExceptionHandler(
            MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>>
    handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error -> {
                    errors.put(
                            error.getField(),
                            error.getDefaultMessage()
                    );
                });

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse>
            handleGenericException(Exception ex,
                                   HttpServletRequest request){

        ErrorResponse error = new ErrorResponse(
                500,
                "Something went wrong: " + ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}
