package co.edu.uniquindio.proyecto.domain.service;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.exception.ReglaNegocioException;
import co.edu.uniquindio.proyecto.domain.valueobject.Prioridad;

/**
 * Servicio de Dominio — Clasificación y asignación de solicitudes.
 *
 * <p>Orquesta dos acciones del agregado en una sola operación atómica:
 * asignar prioridad y asignar responsable. Si cualquiera de las dos
 * falla, ninguna se aplica.</p>
 *
 * <p>No contiene lógica técnica — solo lógica del negocio.</p>
 */
public class AsignacionSolicitudService {

    /**
     * Clasifica una solicitud y le asigna un responsable en una sola operación.
     *
     * @param solicitud             Solicitud a clasificar y asignar.
     * @param prioridad             Nivel de urgencia asignado.
     * @param justificacionPrioridad Razón de la prioridad asignada (RF-03).
     * @param responsable           Usuario que atenderá la solicitud.
     * @throws IllegalArgumentException si la solicitud es nula.
     * @throws ReglaNegocioException    si el responsable no está autorizado.
     */
    public void clasificarYAsignar(Solicitud solicitud,
                                   Prioridad prioridad,
                                   String justificacionPrioridad,
                                   Usuario responsable) {
        if (solicitud == null) {
            throw new IllegalArgumentException("La solicitud no puede ser nula.");
        }
        if (!responsable.puedeSerResponsable()) {
            throw new ReglaNegocioException(
                    "El usuario " + responsable.getNombreCompleto() +
                            " no puede ser asignado como responsable."
            );
        }

        solicitud.asignarPrioridad(prioridad, justificacionPrioridad);
        solicitud.asignarResponsable(responsable);
    }
}