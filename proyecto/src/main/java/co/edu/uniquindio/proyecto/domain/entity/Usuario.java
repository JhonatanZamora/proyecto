package co.edu.uniquindio.proyecto.domain.entity;

import lombok.Getter;

import java.util.UUID;

public class Usuario {

    @Getter
    private final UUID id;
    @Getter
    private final String nombre;
    private boolean activo;

    public Usuario(UUID id, String nombre) {

        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser null");
        }

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        this.id = id;
        this.nombre = nombre;
        this.activo = true;
    }

    public boolean estaActivo() {
        return activo;
    }

    public void desactivar() {
        this.activo = false;
    }
}