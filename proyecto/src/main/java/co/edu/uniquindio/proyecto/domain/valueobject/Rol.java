package co.edu.uniquindio.proyecto.domain.valueobject;

/**
 * Rol de un usuario dentro del sistema académico.
 *
 * Es un enum en valueobject/ porque:
 * - Es inmutable (ya lo garantiza el enum)
 * - Se compara por valor (ya lo garantiza el enum)
 * - Puede ser usado por otras entidades sin depender de Usuario
 *
 * ¿Por qué NO convertirlo en class/record?
 * Porque no tiene estado complejo ni lógica adicional.
 * Un enum ya ES un Value Object en Java.
 */
public enum Rol {
    ESTUDIANTE  ("Estudiante universitario"),
    COORDINADOR ("Coordinador académico"),
    DOCENTE     ("Docente");

    private final String descripcion;

    Rol(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() { return descripcion; }
}