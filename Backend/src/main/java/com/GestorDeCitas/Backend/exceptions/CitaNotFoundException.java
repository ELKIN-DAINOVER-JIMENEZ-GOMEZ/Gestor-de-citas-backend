package com.GestorDeCitas.Backend.exceptions;

public class CitaNotFoundException extends RuntimeException {
    public CitaNotFoundException(String message) {
        super(message);
    }
    public CitaNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
