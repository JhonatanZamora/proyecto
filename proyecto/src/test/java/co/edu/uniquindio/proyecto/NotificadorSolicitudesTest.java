package co.edu.uniquindio.proyecto;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.service.NotificadorSolicitudes;
import co.edu.uniquindio.proyecto.domain.service.NotificadorSolicitudes.TipoNotificacion;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class NotificadorSolicitudesTest {

    private NotificadorSolicitudes notificador;
    private Usuario estudiante;
    private Usuario docente;
    private Solicitud solicitud;

    @BeforeEach
    void setUp() {
        notificador = new NotificadorSolicitudes();
        Email email = new Email("test@uniquindio.edu.co");

        estudiante = new Usuario(
                new DocumentoIdentidad(TipoDocumento.CC, "1111111111"),
                "Juan Pérez", email, Rol.ESTUDIANTE
        );
        docente = new Usuario(
                new DocumentoIdentidad(TipoDocumento.CC, "2222222222"),
                "Carlos López", email, Rol.DOCENTE
        );
        solicitud = new Solicitud(
                estudiante, TipoSolicitud.HOMOLOGACION,
                "Homologación Cálculo I", CanalOrigen.CSU
        );
    }

    @Test
    void nuevaSolicitudDeberiaNotificarAlSolicitante() {
        // Arrange — solicitud recién creada en setUp

        // Act
        List<String> destinatarios = notificador.determinarDestinatarios(
                solicitud, TipoNotificacion.NUEVA_SOLICITUD
        );

        // Assert
        assertTrue(destinatarios.contains(estudiante.getDocumento().numero()));
        assertEquals(1, destinatarios.size());
    }

    @Test
    void asignacionDeberiaNotificarAResponsableYSolicitante() {
        // Arrange
        solicitud.asignarPrioridad(Prioridad.ALTA, "Urgente");
        solicitud.asignarResponsable(docente);

        // Act
        List<String> destinatarios = notificador.determinarDestinatarios(
                solicitud, TipoNotificacion.ASIGNACION
        );

        // Assert
        assertTrue(destinatarios.contains(docente.getDocumento().numero()));
        assertTrue(destinatarios.contains(estudiante.getDocumento().numero()));
        assertEquals(2, destinatarios.size());
    }

    @Test
    void cambioEstadoDeberiaNotificarSoloAlSolicitante() {
        // Arrange — solicitud en cualquier estado

        // Act
        List<String> destinatarios = notificador.determinarDestinatarios(
                solicitud, TipoNotificacion.CAMBIO_ESTADO
        );

        // Assert
        assertTrue(destinatarios.contains(estudiante.getDocumento().numero()));
        assertEquals(1, destinatarios.size());
    }

    @Test
    void cierreDeberiaNotificarASolicitanteYResponsable() {
        // Arrange — ciclo completo hasta CERRADA
        solicitud.asignarPrioridad(Prioridad.MEDIA, "Normal");
        solicitud.asignarResponsable(docente);
        solicitud.marcarComoAtendida("Procesado");
        solicitud.cerrar("Cierre formal");

        // Act
        List<String> destinatarios = notificador.determinarDestinatarios(
                solicitud, TipoNotificacion.CIERRE
        );

        // Assert
        assertTrue(destinatarios.contains(estudiante.getDocumento().numero()));
        assertTrue(destinatarios.contains(docente.getDocumento().numero()));
        assertEquals(2, destinatarios.size());
    }
}