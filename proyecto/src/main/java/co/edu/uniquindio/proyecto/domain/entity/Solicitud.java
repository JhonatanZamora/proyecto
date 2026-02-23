package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.valueobject.*;
import java.time.LocalDateTime;
import java.util.UUID;

public class Solicitud {

    private final UUID id;
    private final LocalDateTime fechaRegistro;
    private final Usuario solicitante;

    private TipoSolicitud tipoSolicitud;
    private Prioridad prioridad;
    private EstadoSolicitud estado;

    public Solicitud(UUID id,
                     Usuario solicitante,
                     TipoSolicitud tipoSolicitud) {

        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser null");
        }

        if (solicitante == null) {
            throw new IllegalArgumentException("El solicitante no puede ser null");
        }

        if (tipoSolicitud == null) {
            throw new IllegalArgumentException("El tipo de solicitud no puede ser null");
        }

        this.id = id;
        this.fechaRegistro = LocalDateTime.now();
        this.solicitante = solicitante;
        this.tipoSolicitud = tipoSolicitud;
        this.estado = new EstadoSolicitud("REGISTRADA");
    }

    public void asignarPrioridad(Prioridad prioridad) {

        if (estado.esCerrada()) {
            throw new IllegalStateException("No se puede modificar una solicitud cerrada");
        }

        this.prioridad = prioridad;
        this.estado = new EstadoSolicitud("CLASIFICADA");
    }

    public void cerrar() {

        if (!estado.valor().equals("ATENDIDA")) {
            throw new IllegalStateException("Solo se puede cerrar una solicitud atendida");
        }

        this.estado = new EstadoSolicitud("CERRADA");
    }

    public void marcarComoAtendida() {

        if (!estado.valor().equals("EN_ATENCION")) {
            throw new IllegalStateException("Solo se puede atender una solicitud en atención");
        }

        this.estado = new EstadoSolicitud("ATENDIDA");
    }
}