package co.edu.uniquindio.proyecto.domain.valueobject;

/**
 * Tipos de solicitud académica reconocidos por el sistema.
 *
 * <p>Catálogo definido en RF-02. Cada tipo determina el tratamiento
 * diferenciado que recibirá la solicitud durante su ciclo de vida.</p>
 */
public enum TipoSolicitud {

    /** Solicitud para registrar o modificar asignaturas en el período vigente. */
    REGISTRO_ASIGNATURA    ("Registro de asignatura"),

    /** Solicitud para homologar una materia cursada en otra institución o programa. */
    HOMOLOGACION           ("Homologación"),

    /** Solicitud para cancelar una asignatura dentro del período académico. */
    CANCELACION_ASIGNATURA ("Cancelación de asignatura"),

    /** Solicitud de cupo en una asignatura con capacidad limitada. */
    SOLICITUD_CUPO         ("Solicitud de cupo"),

    /** Consulta o inquietud académica que no implica trámite formal. */
    CONSULTA_ACADEMICA     ("Consulta académica");

    private final String descripcion;

    TipoSolicitud(String descripcion) { this.descripcion = descripcion; }

    /** @return Descripción legible del tipo de trámite. */
    public String getDescripcion() { return descripcion; }
}