package com.olafo.artesaniasOlafoBackend.exception;

/**
 * Se lanza cuando una operación viola una regla de negocio (responde HTTP 409).
 */
public class ReglaNegocioException extends RuntimeException {
    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }
}
