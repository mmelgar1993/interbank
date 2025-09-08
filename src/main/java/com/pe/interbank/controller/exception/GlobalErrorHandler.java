package com.pe.interbank.controller.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Map<String, String>> manejarErrores(WebExchangeBindException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errores);
    }
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Map<String, String>>> manejarErroresGenerales(Exception ex) {
        Map<String, String> error = Map.of("mensaje", ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error));
    }

    @ExceptionHandler(ServerWebInputException.class)
    public Mono<ResponseEntity<String>> handleInvalidFormat(ServerWebInputException ex) {
        return Mono.just(ResponseEntity.badRequest().body("Formato inválido: se esperaban valores numéricos"));
    }


}