package org.our_place.ourplacebackend.config;

import org.our_place.shared.infra.http.ResultException;
import org.our_place.shared.utils.ResultIssue;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Environment env;

    public GlobalExceptionHandler(Environment env) {
        this.env = env;
    }

    // errores de dominio
    @ExceptionHandler(ResultException.class)
    public ResponseEntity<Map<String, Object>> handleResultException(ResultException ex) {
        ResultIssue issue = ex.getIssue();
        HttpStatus status = issue.severity() == ResultIssue.Severity.CRITICAL
                ? HttpStatus.INTERNAL_SERVER_ERROR
                : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(Map.of(
                "success", false,
                "code", issue.code(),
                "message", issue.message(),
                "timestamp", Instant.now().toString()
        ));
    }

    // Otros errores inesperados
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("success", false);
        body.put("code", "UNEXPECTED_ERROR");
        body.put("timestamp", Instant.now().toString());

        // Verificamos si el perfil activo es "dev" o "test"
        if (env.acceptsProfiles(Profiles.of("dev", "test"))) {
            // En desarrollo/test, sí enviamos el mensaje real para depurar
            body.put("message", ex.getMessage());
            body.put("exception", ex.getClass().getName()); // útil saber qué excepción exacta fue
        } else {
            // En prod, ocultamos el detalle
            body.put("message", "Ocurrió un error interno inesperado");
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
