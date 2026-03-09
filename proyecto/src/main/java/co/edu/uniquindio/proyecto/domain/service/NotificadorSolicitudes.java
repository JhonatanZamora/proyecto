package co.edu.uniquindio.proyecto.domain.service;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio de Dominio — Determina los destinatarios de notificaciones.
 *
 * <p>La lógica de "a quién notificar" es una regla del negocio,
 * no una decisión técnica. Por eso vive en el dominio.</p>
 *
 * <p>Este servicio decide los destinatarios según las reglas del negocio
 * pero no envía notificaciones — eso es responsabilidad de infraestructura.</p>
 */
public class NotificadorSolicitudes {

    /**
     * Tipos de evento que generan una notificación.
     */
    public enum TipoNotificacion {
        NUEVA_SOLICITUD,
        ASIGNACION,
        CAMBIO_ESTADO,
        CIERRE
    }

    /**
     * Determina los documentos de los usuarios a notificar
     * según el tipo de evento ocurrido sobre la solicitud.
     *
     * @param solicitud La solicitud sobre la que ocurrió el evento.
     * @param tipo      El tipo de evento ocurrido.
     * @return Lista de números de documento de los usuarios a notificar.
     */
    public List<String> determinarDestinatarios(Solicitud solicitud,
                                                TipoNotificacion tipo) {
        List<String> destinatarios = new ArrayList<>();

        switch (tipo) {

            case NUEVA_SOLICITUD -> {
                destinatarios.add(solicitud.getSolicitante().getDocumento().numero());
            }

            case ASIGNACION -> {
                if (solicitud.getResponsable() != null) {
                    destinatarios.add(solicitud.getResponsable().getDocumento().numero());
                }
                destinatarios.add(solicitud.getSolicitante().getDocumento().numero());
            }

            case CAMBIO_ESTADO -> {
                destinatarios.add(solicitud.getSolicitante().getDocumento().numero());
            }

            case CIERRE -> {
                destinatarios.add(solicitud.getSolicitante().getDocumento().numero());
                if (solicitud.getResponsable() != null) {
                    destinatarios.add(solicitud.getResponsable().getDocumento().numero());
                }
            }
        }

        return destinatarios;
    }
}