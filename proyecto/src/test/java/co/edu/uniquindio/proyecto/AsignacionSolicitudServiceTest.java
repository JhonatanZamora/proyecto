package co.edu.uniquindio.proyecto;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.exception.ReglaNegocioException;
import co.edu.uniquindio.proyecto.domain.service.AsignacionSolicitudService;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AsignacionSolicitudServiceTest {

    private AsignacionSolicitudService service;
    private Usuario estudiante;
    private Usuario docente;
    private Usuario coordinador;
    private Solicitud solicitud;

    @BeforeEach
    void setUp() {
        service = new AsignacionSolicitudService();
        Email email = new Email("test@uniquindio.edu.co");

        estudiante = new Usuario(
                new DocumentoIdentidad(TipoDocumento.CC, "1111111111"),
                "Juan Pérez", email, Rol.ESTUDIANTE
        );
        docente = new Usuario(
                new DocumentoIdentidad(TipoDocumento.CC, "2222222222"),
                "Carlos López", email, Rol.DOCENTE
        );
        coordinador = new Usuario(
                new DocumentoIdentidad(TipoDocumento.CC, "3333333333"),
                "Ana Gómez", email, Rol.COORDINADOR
        );
        solicitud = new Solicitud(
                estudiante, TipoSolicitud.SOLICITUD_CUPO,
                "Cupo en Programación Avanzada", CanalOrigen.SAC
        );
    }

    @Test
    void deberiaClasificarYAsignarEnUnaOperacion() {
        // Arrange — solicitud ya creada en setUp

        // Act
        service.clasificarYAsignar(solicitud, Prioridad.ALTA,
                "Impacto en continuidad académica", docente);

        // Assert
        assertEquals(EstadoSolicitud.EN_ATENCION, solicitud.getEstado());
        assertEquals(docente, solicitud.getResponsable());
        assertEquals(Prioridad.ALTA, solicitud.getPrioridad());
    }

    @Test
    void deberiaPermitirCoordinadorComoResponsable() {
        // Arrange — solicitud ya creada en setUp

        // Act
        service.clasificarYAsignar(solicitud, Prioridad.MEDIA,
                "Trámite estándar", coordinador);

        // Assert
        assertEquals(coordinador, solicitud.getResponsable());
    }

    @Test
    void noDeberiaAsignarEstudianteComoResponsable() {
        // Arrange — solicitud ya creada en setUp

        // Act & Assert
        assertThrows(ReglaNegocioException.class, () ->
                service.clasificarYAsignar(solicitud, Prioridad.BAJA,
                        "Sin urgencia", estudiante)
        );
    }

    @Test
    void deberiaLanzarErrorSiSolicitudEsNula() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                service.clasificarYAsignar(null, Prioridad.ALTA, "Justificación", docente)
        );
    }
}