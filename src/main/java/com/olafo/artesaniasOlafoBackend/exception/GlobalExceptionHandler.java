package com.olafo.artesaniasOlafoBackend.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manejo centralizado de errores: convierte las excepciones en respuestas
 * JSON claras (formato ProblemDetail) con el código HTTP adecuado.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /** 404: el recurso no existe. */
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ProblemDetail> noEncontrado(RecursoNoEncontradoException ex) {
        return respuesta(HttpStatus.NOT_FOUND, "Recurso no encontrado", ex.getMessage());
    }

    /** 409: se viola una regla de negocio (por ejemplo, nombre duplicado). */
    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<ProblemDetail> reglaNegocio(ReglaNegocioException ex) {
        return respuesta(HttpStatus.CONFLICT, "Regla de negocio", ex.getMessage());
    }

    /** 400: los datos enviados no pasan las validaciones (@Valid). */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> validacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(e -> errores.put(e.getField(), e.getDefaultMessage()));

        ProblemDetail problema = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Los datos enviados no son válidos");
        problema.setTitle("Error de validación");
        problema.setProperty("errores", errores);
        return ResponseEntity.badRequest().body(problema);
    }

    /** 400: el cuerpo de la petición no es un JSON legible. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> jsonInvalido(HttpMessageNotReadableException ex) {
        return respuesta(HttpStatus.BAD_REQUEST, "Cuerpo inválido",
                "El cuerpo de la petición no es un JSON válido");
    }

    /**
     * 409: la base de datos rechazó la operación (llave foránea, valor único,
     * etc.).
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> integridad(DataIntegrityViolationException ex) {
        return respuesta(HttpStatus.CONFLICT, "Integridad de datos",
                "La operación viola una restricción de la base de datos");
    }

    private ResponseEntity<ProblemDetail> respuesta(HttpStatus estado, String titulo, String detalle) {
        ProblemDetail problema = ProblemDetail.forStatusAndDetail(estado, detalle);
        problema.setTitle(titulo);
        return ResponseEntity.status(estado).body(problema);
    }
}
