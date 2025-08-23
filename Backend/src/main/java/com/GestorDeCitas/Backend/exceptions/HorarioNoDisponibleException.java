package com.GestorDeCitas.Backend.exceptions;

public class HorarioNoDisponibleException extends RuntimeException {
    public HorarioNoDisponibleException(String message) {
        super(message);
    }
    public HorarioNoDisponibleException(String message, Throwable cause) {
        super(message, cause);
    }
}
