package co.edu.uniquindio.proyecto.domain.valueobject;

/**
 * Estado de una solicitud académica a lo largo de su ciclo de vida.
 *
 * <p>Estados definidos en RF-04. El ciclo válido es:</p>
 * <pre>
 *   REGISTRADA → CLASIFICADA → EN_ATENCION → ATENDIDA → CERRADA
 * </pre>
 *
 * <p>Los estados terminales no admiten más transiciones.
 * La entidad {@code Solicitud} consulta {@link #esTerminal()} antes
 * de permitir cualquier modificación.</p>
 */
public enum EstadoSolicitud {

    /** Solicitud recién registrada, sin clasificar ni priorizar. */
    REGISTRADA  ("Registrada, pendiente de clasificación", false),

    /** Prioridad y tipo confirmados por el coordinador. */
    CLASIFICADA ("Clasificada con prioridad asignada",     false),

    /** Responsable asignado, solicitud en proceso de atención. */
    EN_ATENCION ("En atención por responsable asignado",   false),

    /** Atendida por el responsable, pendiente de cierre formal. */
    ATENDIDA    ("Atendida, pendiente de cierre",          false),

    /** Cerrada definitivamente. No admite más modificaciones (RF-08). */
    CERRADA     ("Cerrada definitivamente",                true);

    private final String  descripcion;
    private final boolean terminal;

    EstadoSolicitud(String descripcion, boolean terminal) {
        this.descripcion = descripcion;
        this.terminal    = terminal;
    }

    /** @return Descripción legible del estado. */
    public String getDescripcion() { return descripcion; }

    /**
     * Indica si este estado es el final del ciclo de vida.
     *
     * @return {@code true} si la solicitud no puede recibir más cambios.
     */
    public boolean esTerminal() { return terminal; }
}
