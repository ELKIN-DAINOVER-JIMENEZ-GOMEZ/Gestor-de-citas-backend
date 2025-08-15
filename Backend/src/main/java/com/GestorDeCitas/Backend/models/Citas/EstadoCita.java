package com.GestorDeCitas.Backend.models.Citas;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EstadoCita {
    PENDIENTE("Pendiente"),
    CONFIRMADA("Confirmada"),
    COMPLETADA ("Completada"),
    CANCELADA ("Cancelada"),
    NO_ASISTIO ("No asisitio");

    private final String descripco;

    public String getDescripco() {
        return descripco;
    }
}
