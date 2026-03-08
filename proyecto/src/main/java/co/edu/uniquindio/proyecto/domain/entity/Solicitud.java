package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.exception.ReglaNegocioException;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Agregado Raíz — Solicitud Académica.
 *
 * <p>Entidad central del sistema. Controla todo lo que ocurre dentro del agregado:
 * estado, prioridad, responsable, canal de origen e historial de acciones.</p>
 *
 * <p>Invariantes garantizadas (Guía Maestra):</p>
 * <ul>
 *   <li>RF-01: Toda solicitud almacena descripción, canal de origen y solicitante.</li>
 *   <li>RF-03: La prioridad se registra junto con su justificación.</li>
 *   <li>RF-04: Las transiciones de estado son siempre coherentes.</li>
 *   <li>RF-05: Solo usuarios autorizados pueden ser responsables.</li>
 *   <li>RF-06: Toda acción queda registrada en el historial con observaciones.</li>
 *   <li>RF-08: El cierre requiere observación y solo aplica desde ATENDIDA.</li>
 * </ul>
 *
 * <p>Ciclo de vida:</p>
 * <pre>
 *   REGISTRADA → CLASIFICADA → EN_ATENCION → ATENDIDA → CERRADA
 * </pre>
 */
@Getter
public class Solicitud {

    /** Identificador único de la solicitud en el dominio. */
    private final CodigoSolicitud codigo;

    /** Fecha y hora de registro — inmutable, el pasado no cambia. */
    private final LocalDateTime fechaRegistro;

    /** Usuario que originó la solicitud — no cambia de dueño. */
    private final Usuario solicitante;

    /** Descripción del trámite ingresada por el solicitante (RF-01). */
    private final String descripcion;

    /** Canal a través del cual llegó la solicitud (RF-01). */
    private final CanalOrigen canalOrigen;

    /** Tipo de trámite académico. */
    private TipoSolicitud tipoSolicitud;

    /** Nivel de urgencia asignado durante la clasificación. */
    private Prioridad prioridad;

    /** Justificación de la prioridad asignada (RF-03). */
    private String justificacionPrioridad;

    /** Estado actual dentro del ciclo de vida. */
    private EstadoSolicitud estado;

    /** Usuario responsable de atender la solicitud. */
    private Usuario responsable;

    /** Historial de todas las acciones realizadas sobre la solicitud (RF-06). */
    private final List<EventoHistorial> historial;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /**
     * Registra una nueva solicitud académica en estado {@code REGISTRADA}.
     *
     * @param solicitante  Usuario que registra la solicitud.
     * @param tipoSolicitud Tipo de trámite académico.
     * @param descripcion  Descripción del trámite (RF-01).
     * @param canalOrigen  Canal por el que llegó la solicitud (RF-01).
     * @throws ReglaNegocioException si el solicitante no tiene rol ESTUDIANTE.
     */
    public Solicitud(Usuario solicitante,
                     TipoSolicitud tipoSolicitud,
                     String descripcion,
                     CanalOrigen canalOrigen) {

        if (solicitante == null) {
            throw new IllegalArgumentException("El solicitante es obligatorio.");
        }
        if (tipoSolicitud == null) {
            throw new IllegalArgumentException("El tipo de solicitud es obligatorio.");
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción es obligatoria (RF-01).");
        }
        if (canalOrigen == null) {
            throw new IllegalArgumentException("El canal de origen es obligatorio (RF-01).");
        }
        if (!solicitante.puedeRegistrarSolicitudes()) {
            throw new ReglaNegocioException(
                    "Solo los estudiantes pueden registrar solicitudes (RF-13)."
            );
        }

        this.codigo        = CodigoSolicitud.generar();
        this.fechaRegistro = LocalDateTime.now();
        this.solicitante   = solicitante;
        this.tipoSolicitud = tipoSolicitud;
        this.descripcion   = descripcion.trim();
        this.canalOrigen   = canalOrigen;
        this.estado        = EstadoSolicitud.REGISTRADA;
        this.historial     = new ArrayList<>();

        registrarEvento(
                null,
                EstadoSolicitud.REGISTRADA,
                "Solicitud registrada.",
                "Canal: " + canalOrigen.getDescripcion(),
                solicitante.getDocumento().numero()
        );
    }

    // -------------------------------------------------------------------------
    // Comportamientos del dominio
    // -------------------------------------------------------------------------

    /**
     * Clasifica la solicitud asignando prioridad y su justificación.
     *
     * <p>Cumple RF-03: la prioridad queda registrada junto con su justificación.</p>
     *
     * @param prioridad             Nivel de urgencia asignado.
     * @param justificacionPrioridad Razón por la que se asigna esa prioridad.
     * @throws ReglaNegocioException si la solicitud está en estado terminal.
     */
    public void asignarPrioridad(Prioridad prioridad, String justificacionPrioridad) {
        validarQueNoEsTerminal();
        if (prioridad == null) {
            throw new IllegalArgumentException("La prioridad no puede ser nula.");
        }
        if (justificacionPrioridad == null || justificacionPrioridad.isBlank()) {
            throw new IllegalArgumentException(
                    "La justificación de la prioridad es obligatoria (RF-03)."
            );
        }

        EstadoSolicitud anterior = this.estado;
        this.prioridad              = prioridad;
        this.justificacionPrioridad = justificacionPrioridad.trim();
        this.estado                 = EstadoSolicitud.CLASIFICADA;

        registrarEvento(
                anterior,
                this.estado,
                "Prioridad asignada: " + prioridad.getDescripcion(),
                justificacionPrioridad,
                "SISTEMA"
        );
    }

    /**
     * Asigna un responsable y mueve la solicitud a {@code EN_ATENCION}.
     *
     * <p>Cumple RF-05: el responsable debe estar activo y la asignación
     * queda registrada en el historial.</p>
     *
     * @param responsable Usuario que atenderá la solicitud.
     * @throws ReglaNegocioException si la solicitud no está CLASIFICADA
     *                               o el usuario no puede ser responsable.
     */
    public void asignarResponsable(Usuario responsable) {
        validarQueNoEsTerminal();
        if (estado != EstadoSolicitud.CLASIFICADA) {
            throw new ReglaNegocioException(
                    "La solicitud debe estar CLASIFICADA antes de asignar responsable."
            );
        }
        if (responsable == null || !responsable.puedeSerResponsable()) {
            throw new ReglaNegocioException(
                    "Solo docentes o coordinadores pueden ser responsables (RF-05)."
            );
        }

        EstadoSolicitud anterior = this.estado;
        this.responsable = responsable;
        this.estado      = EstadoSolicitud.EN_ATENCION;

        registrarEvento(
                anterior,
                this.estado,
                "Responsable asignado: " + responsable.getNombreCompleto(),
                null,
                responsable.getDocumento().numero()
        );
    }

    /**
     * Marca la solicitud como atendida por el responsable.
     *
     * @param observaciones Observaciones del responsable al atender.
     * @throws ReglaNegocioException si la solicitud no está EN_ATENCION.
     */
    public void marcarComoAtendida(String observaciones) {
        validarQueNoEsTerminal();
        if (estado != EstadoSolicitud.EN_ATENCION) {
            throw new ReglaNegocioException(
                    "Solo se puede atender una solicitud en estado EN_ATENCION."
            );
        }

        EstadoSolicitud anterior = this.estado;
        this.estado = EstadoSolicitud.ATENDIDA;

        registrarEvento(
                anterior,
                this.estado,
                "Solicitud marcada como atendida.",
                observaciones,
                responsable.getDocumento().numero()
        );
    }

    /**
     * Cierra definitivamente la solicitud.
     *
     * <p>Cumple RF-08: solo es posible desde ATENDIDA y requiere
     * una observación de cierre obligatoria.</p>
     *
     * @param observacionCierre Observación formal que justifica el cierre.
     * @throws ReglaNegocioException si la solicitud no está ATENDIDA
     *                               o la observación está vacía.
     */
    public void cerrar(String observacionCierre) {
        validarQueNoEsTerminal();
        if (estado != EstadoSolicitud.ATENDIDA) {
            throw new ReglaNegocioException(
                    "Solo se puede cerrar una solicitud en estado ATENDIDA (RF-08)."
            );
        }
        if (observacionCierre == null || observacionCierre.isBlank()) {
            throw new ReglaNegocioException(
                    "La observación de cierre es obligatoria (RF-08)."
            );
        }

        EstadoSolicitud anterior = this.estado;
        this.estado = EstadoSolicitud.CERRADA;

        registrarEvento(
                anterior,
                this.estado,
                "Solicitud cerrada definitivamente.",
                observacionCierre,
                responsable.getDocumento().numero()
        );
    }

    /**
     * Devuelve el historial como lista de solo lectura.
     *
     * <p>Nadie puede agregar eventos directamente — solo la raíz del agregado.</p>
     *
     * @return Vista inmutable del historial de la solicitud.
     */
    public List<EventoHistorial> getHistorial() {
        return Collections.unmodifiableList(historial);
    }

    // -------------------------------------------------------------------------
    // Métodos privados
    // -------------------------------------------------------------------------

    /**
     * Crea y agrega un evento al historial de la solicitud.
     *
     * <p>Método privado que garantiza que estado e historial siempre
     * estén sincronizados — nadie más puede escribir en el historial.</p>
     */
    private void registrarEvento(EstadoSolicitud anterior,
                                 EstadoSolicitud nuevo,
                                 String descripcion,
                                 String observaciones,
                                 String documentoResponsable) {
        historial.add(new EventoHistorial(
                anterior, nuevo, descripcion, observaciones, documentoResponsable
        ));
    }

    /**
     * Verifica que la solicitud no esté en un estado terminal.
     *
     * @throws ReglaNegocioException si el estado actual es terminal.
     */
    private void validarQueNoEsTerminal() {
        if (this.estado.esTerminal()) {
            throw new ReglaNegocioException(
                    "La solicitud '" + this.codigo + "' está cerrada y no puede modificarse."
            );
        }
    }

    // -------------------------------------------------------------------------
    // Igualdad por identidad
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Solicitud s)) return false;
        return Objects.equals(codigo, s.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return "Solicitud{codigo=" + codigo +
                ", tipo=" + tipoSolicitud +
                ", estado=" + estado +
                ", canal=" + canalOrigen +
                ", eventos=" + historial.size() + "}";
    }
}