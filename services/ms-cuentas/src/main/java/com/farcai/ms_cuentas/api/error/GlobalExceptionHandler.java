package com.farcai.ms_cuentas.api.error;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.farcai.ms_cuentas.domain.exceptions.SaldoNoDisponibleException;
import com.farcai.ms_cuentas.domain.exceptions.ValidationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SaldoNoDisponibleException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiError saldo(SaldoNoDisponibleException ex) {
        return new ApiError("SALDO_NO_DISPONIBLE", ex.getMessage(), Instant.now());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError validation(ValidationException ex) {
        return new ApiError("VALIDATION_ERROR", ex.getMessage(), Instant.now());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError dtoValidation(MethodArgumentNotValidException ex) {
        return new ApiError("VALIDATION_ERROR", "Request inválido", Instant.now());
    }

}
