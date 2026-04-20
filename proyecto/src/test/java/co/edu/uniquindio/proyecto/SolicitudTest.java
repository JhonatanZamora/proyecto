package co.edu.uniquindio.proyecto;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.exception.ReglaNegocioException;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SolicitudTest {

    private Usuario estudiante;
    private Usuario docente;
    private Usuario coordinador;

    @BeforeEach
    void setUp() {
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
    }

    private Solicitud crearSolicitudValida() {
        return new Solicitud(
                estudiante,
                TipoSolicitud.HOMOLOGACION,
                "Solicitud de homologación de Cálculo I",
                CanalOrigen.CSU
        );
    }

    private Solicitud crearSolicitudAtendida() {
        Solicitud s = crearSolicitudValida();
        s.asignarPrioridad(Prioridad.ALTA, "Impacto académico alto");
        s.asignarResponsable(docente);
        s.marcarComoAtendida("Trámite procesado correctamente");
        return s;
    }

    // -------------------------------------------------------------------------
    // Construcción válida
    // -------------------------------------------------------------------------

    @Test
    void deberiaCrearSolicitudEnEstadoRegistrada() {
        // Arrange & Act
        Solicitud solicitud = crearSolicitudValida();

        // Assert
        assertEquals(EstadoSolicitud.REGISTRADA, solicitud.getEstado());
        assertNotNull(solicitud.getCodigo());
        assertNotNull(solicitud.getFechaRegistro());
    }

    @Test
    void deberiaRegistrarEventoAlCrearse() {
        // Arrange & Act
        Solicitud solicitud = crearSolicitudValida();

        // Assert
        assertEquals(1, solicitud.getHistorial().size());
        assertEquals(EstadoSolicitud.REGISTRADA,
                solicitud.getHistorial().get(0).estadoNuevo());
    }

    // -------------------------------------------------------------------------
    // Construcción inválida — RF-01
    // -------------------------------------------------------------------------

    @Test
    void deberiaLanzarErrorSiSolicitanteEsNulo() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Solicitud(null, TipoSolicitud.HOMOLOGACION, "desc", CanalOrigen.CSU)
        );
    }

    @Test
    void deberiaLanzarErrorSiDescripcionEsVacia() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Solicitud(estudiante, TipoSolicitud.HOMOLOGACION, "", CanalOrigen.CSU)
        );
    }

    @Test
    void deberiaLanzarErrorSiCanalOrigenEsNulo() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Solicitud(estudiante, TipoSolicitud.HOMOLOGACION, "desc", null)
        );
    }

    @Test
    void soloEstudiantePuedeRegistrarSolicitud() {
        // Arrange, Act & Assert — RF-13
        assertThrows(ReglaNegocioException.class, () ->
                new Solicitud(docente, TipoSolicitud.HOMOLOGACION, "desc", CanalOrigen.CSU)
        );
    }

    // -------------------------------------------------------------------------
    // Ciclo de vida — transiciones válidas
    // -------------------------------------------------------------------------

    @Test
    void deberiaAvanzarAClasificadaAlAsignarPrioridad() {
        // Arrange
        Solicitud solicitud = crearSolicitudValida();

        // Act
        solicitud.asignarPrioridad(Prioridad.ALTA, "Impacto en graduación");

        // Assert
        assertEquals(EstadoSolicitud.CLASIFICADA, solicitud.getEstado());
        assertEquals(Prioridad.ALTA, solicitud.getPrioridad());
    }

    @Test
    void deberiaAvanzarAEnAtencionAlAsignarResponsable() {
        // Arrange
        Solicitud solicitud = crearSolicitudValida();
        solicitud.asignarPrioridad(Prioridad.MEDIA, "Prioridad normal");

        // Act
        solicitud.asignarResponsable(docente);

        // Assert
        assertEquals(EstadoSolicitud.EN_ATENCION, solicitud.getEstado());
        assertEquals(docente, solicitud.getResponsable());
    }

    @Test
    void deberiaAvanzarAAtendida() {
        // Arrange
        Solicitud solicitud = crearSolicitudValida();
        solicitud.asignarPrioridad(Prioridad.BAJA, "Sin urgencia");
        solicitud.asignarResponsable(docente);

        // Act
        solicitud.marcarComoAtendida("Trámite procesado");

        // Assert
        assertEquals(EstadoSolicitud.ATENDIDA, solicitud.getEstado());
    }

    @Test
    void deberiaCerrarSolicitudAtendida() {
        // Arrange
        Solicitud solicitud = crearSolicitudAtendida();

        // Act
        solicitud.cerrar("Proceso completado satisfactoriamente");

        // Assert
        assertEquals(EstadoSolicitud.CERRADA, solicitud.getEstado());
        assertTrue(solicitud.getEstado().esTerminal());
    }

    // -------------------------------------------------------------------------
    // Invariantes — transiciones inválidas
    // -------------------------------------------------------------------------

    @Test
    void noDeberiaAsignarPrioridadSinJustificacion() {
        // Arrange
        Solicitud solicitud = crearSolicitudValida();
        EstadoSolicitud estadoAntes = solicitud.getEstado();

        // Act & Assert — RF-03
        assertThrows(IllegalArgumentException.class, () ->
                solicitud.asignarPrioridad(Prioridad.ALTA, "")
        );
        assertEquals(estadoAntes, solicitud.getEstado());
    }

    @Test
    void noDeberiaAsignarResponsableSinClasificarPrimero() {
        // Arrange
        Solicitud solicitud = crearSolicitudValida();

        // Act & Assert
        assertThrows(ReglaNegocioException.class, () ->
                solicitud.asignarResponsable(docente)
        );
        assertEquals(EstadoSolicitud.REGISTRADA, solicitud.getEstado());
    }

    @Test
    void noDeberiaAsignarEstudianteComoResponsable() {
        // Arrange
        Solicitud solicitud = crearSolicitudValida();
        solicitud.asignarPrioridad(Prioridad.ALTA, "Urgente");

        // Act & Assert — RF-05
        assertThrows(ReglaNegocioException.class, () ->
                solicitud.asignarResponsable(estudiante)
        );
        assertEquals(EstadoSolicitud.CLASIFICADA, solicitud.getEstado());
    }

    @Test
    void noDeberiaCerrarSinObservacion() {
        // Arrange
        Solicitud solicitud = crearSolicitudAtendida();

        // Act & Assert — RF-08
        assertThrows(ReglaNegocioException.class, () ->
                solicitud.cerrar("")
        );
        assertEquals(EstadoSolicitud.ATENDIDA, solicitud.getEstado());
    }

    @Test
    void noDeberiaCerrarSolicitudQueNoEstaAtendida() {
        // Arrange
        Solicitud solicitud = crearSolicitudValida();
        solicitud.asignarPrioridad(Prioridad.ALTA, "Urgente");
        solicitud.asignarResponsable(docente);

        // Act & Assert — RF-08
        assertThrows(ReglaNegocioException.class, () ->
                solicitud.cerrar("Observación")
        );
        assertEquals(EstadoSolicitud.EN_ATENCION, solicitud.getEstado());
    }

    @Test
    void noDeberiaModificarSolicitudCerrada() {
        // Arrange
        Solicitud solicitud = crearSolicitudAtendida();
        solicitud.cerrar("Cierre formal");

        // Act & Assert — toda acción sobre solicitud cerrada debe fallar
        assertThrows(ReglaNegocioException.class, () ->
                solicitud.asignarPrioridad(Prioridad.ALTA, "Intento inválido")
        );
        assertThrows(ReglaNegocioException.class, () ->
                solicitud.asignarResponsable(docente)
        );
        assertThrows(ReglaNegocioException.class, () ->
                solicitud.marcarComoAtendida("Intento inválido")
        );
        assertThrows(ReglaNegocioException.class, () ->
                solicitud.cerrar("Doble cierre")
        );
    }

    // -------------------------------------------------------------------------
    // Historial — RF-06
    // -------------------------------------------------------------------------

    @Test
    void historialDeberiaSerInmutableDesdeAfuera() {
        // Arrange
        Solicitud solicitud = crearSolicitudValida();

        // Act & Assert
        assertThrows(UnsupportedOperationException.class, () ->
                solicitud.getHistorial().clear()
        );
    }

    @Test
    void cadaAccionDeberiaAgregarUnEventoAlHistorial() {
        // Arrange
        Solicitud solicitud = crearSolicitudValida();         // 1 evento

        // Act
        solicitud.asignarPrioridad(Prioridad.ALTA, "J");     // 2 eventos
        solicitud.asignarResponsable(coordinador);           // 3 eventos
        solicitud.marcarComoAtendida("Ok");                  // 4 eventos
        solicitud.cerrar("Cierre");                          // 5 eventos

        // Assert
        assertEquals(5, solicitud.getHistorial().size());
    }
}