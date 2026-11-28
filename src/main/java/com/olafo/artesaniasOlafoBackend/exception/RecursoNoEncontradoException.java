package com.olafo.artesaniasOlafoBackend.exception;

/** Se lanza cuando un registro solicitado no existe (responde HTTP 404). */
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
