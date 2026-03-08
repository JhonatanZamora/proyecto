package co.edu.uniquindio.proyecto.domain.valueobject;

/**
 * Canal a través del cual fue recibida una solicitud académica.
 *
 * <p>Campo obligatorio en el registro de solicitudes según RF-01.
 * Permite identificar la procedencia de cada solicitud y centralizar
 * las que llegan por múltiples vías.</p>
 */
public enum CanalOrigen {

    /** Centro de Servicios Universitarios — atención presencial. */
    CSU        ("Centro de Servicios Universitarios"),

    /** Solicitud recibida por correo electrónico institucional. */
    CORREO     ("Correo electrónico"),

    /** Sistema de Atención al Cliente institucional. */
    SAC        ("Sistema de Atención al Cliente"),

    /** Solicitud recibida vía llamada telefónica. */
    TELEFONICO ("Atención telefónica");

    private final String descripcion;

    CanalOrigen(String descripcion) { this.descripcion = descripcion; }

    /** @return Descripción legible del canal. */
    public String getDescripcion() { return descripcion; }
}