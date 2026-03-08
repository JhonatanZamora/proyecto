package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.valueobject.EstadoSolicitud;

import java.time.LocalDateTime;

/**
 * Registro inmutable de una acción ocurrida en el ciclo de vida de una {@link Solicitud}.
 *
 * <p>Cumple RF-06: cada evento almacena fecha, acción, usuario responsable
 * y observaciones asociadas. Garantiza la trazabilidad completa de la solicitud.</p>
 *
 * <p>Solo la raíz del agregado ({@code Solicitud}) puede crear instancias.
 * El constructor tiene visibilidad de paquete para reforzar este contrato.</p>
 */
public class EventoHistorial {

    /** Estado en que estaba la solicitud antes de la acción. */
    private final EstadoSolicitud estadoAnterior;

    /** Estado al que transicionó la solicitud tras la acción. */
    private final EstadoSolicitud estadoNuevo;

    /** Descripción de la acción realizada. */
    private final String descripcion;

    /** Observaciones adicionales registradas por el usuario (RF-06). */
    private final String observaciones;

    /** Momento exacto en que ocurrió la acción. */
    private final LocalDateTime fechaEvento;

    /** Documento del usuario que realizó la acción. */
    private final String documentoResponsable;

    /**
     * Constructor de visibilidad de paquete.
     * Solo {@link Solicitud} puede instanciar eventos de historial.
     *
     * @param estadoAnterior       Estado previo a la acción.
     * @param estadoNuevo          Estado resultante de la acción.
     * @param descripcion          Descripción de la acción realizada.
     * @param observaciones        Observaciones opcionales del responsable.
     * @param documentoResponsable Documento del usuario que ejecutó la acción.
     */
    EventoHistorial(EstadoSolicitud estadoAnterior,
                    EstadoSolicitud estadoNuevo,
                    String descripcion,
                    String observaciones,
                    String documentoResponsable) {
        this.estadoAnterior       = estadoAnterior;
        this.estadoNuevo          = estadoNuevo;
        this.descripcion          = descripcion;
        this.observaciones        = observaciones;
        this.fechaEvento          = LocalDateTime.now();
        this.documentoResponsable = documentoResponsable;
    }

    public EstadoSolicitud getEstadoAnterior()  { return estadoAnterior; }
    public EstadoSolicitud getEstadoNuevo()     { return estadoNuevo; }
    public String getDescripcion()              { return descripcion; }
    public String getObservaciones()            { return observaciones; }
    public LocalDateTime getFechaEvento()       { return fechaEvento; }
    public String getDocumentoResponsable()     { return documentoResponsable; }

    @Override
    public String toString() {
        return "EventoHistorial{" + fechaEvento + " | " +
                estadoAnterior + " → " + estadoNuevo + " | " + descripcion + "}";
    }
}
