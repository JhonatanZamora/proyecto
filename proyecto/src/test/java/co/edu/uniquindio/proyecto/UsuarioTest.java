package co.edu.uniquindio.proyecto;

import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {

    private DocumentoIdentidad docEstudiante;
    private DocumentoIdentidad docDocente;
    private DocumentoIdentidad docCoordinador;
    private Email email;

    @BeforeEach
    void setUp() {
        docEstudiante  = new DocumentoIdentidad(TipoDocumento.CC, "1111111111");
        docDocente     = new DocumentoIdentidad(TipoDocumento.CC, "2222222222");
        docCoordinador = new DocumentoIdentidad(TipoDocumento.CC, "3333333333");
        email          = new Email("test@uniquindio.edu.co");
    }

    // -------------------------------------------------------------------------
    // Construcción válida
    // -------------------------------------------------------------------------

    @Test
    void deberiaCrearEstudianteCorrectamente() {
        // Arrange & Act
        Usuario estudiante = new Usuario(docEstudiante, "Juan Pérez", email, Rol.ESTUDIANTE);

        // Assert
        assertEquals(docEstudiante, estudiante.getDocumento());
        assertEquals("Juan Pérez", estudiante.getNombreCompleto());
        assertEquals(Rol.ESTUDIANTE, estudiante.getRol());
    }

    @Test
    void deberiaEliminarEspaciosDelNombre() {
        // Arrange & Act
        Usuario usuario = new Usuario(docEstudiante, "  Juan Pérez  ", email, Rol.ESTUDIANTE);

        // Assert
        assertEquals("Juan Pérez", usuario.getNombreCompleto());
    }

    // -------------------------------------------------------------------------
    // Construcción inválida
    // -------------------------------------------------------------------------

    @Test
    void deberiaLanzarErrorSiDocumentoEsNulo() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Usuario(null, "Juan", email, Rol.ESTUDIANTE)
        );
    }

    @Test
    void deberiaLanzarErrorSiNombreEsVacio() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Usuario(docEstudiante, "", email, Rol.ESTUDIANTE)
        );
    }

    @Test
    void deberiaLanzarErrorSiEmailEsNulo() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Usuario(docEstudiante, "Juan", null, Rol.ESTUDIANTE)
        );
    }

    @Test
    void deberiaLanzarErrorSiRolEsNulo() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new Usuario(docEstudiante, "Juan", email, null)
        );
    }

    // -------------------------------------------------------------------------
    // Permisos por rol — RF-13
    // -------------------------------------------------------------------------

    @Test
    void estudianteSoloPuedeRegistrarSolicitudes() {
        // Arrange
        Usuario estudiante = new Usuario(docEstudiante, "Juan", email, Rol.ESTUDIANTE);

        // Assert
        assertTrue(estudiante.puedeRegistrarSolicitudes());
        assertFalse(estudiante.puedeSerResponsable());
        assertFalse(estudiante.puedeClasificarSolicitudes());
    }

    @Test
    void docenteSoloPuedeSerResponsable() {
        // Arrange
        Usuario docente = new Usuario(docDocente, "Carlos", email, Rol.DOCENTE);

        // Assert
        assertTrue(docente.puedeSerResponsable());
        assertFalse(docente.puedeRegistrarSolicitudes());
        assertFalse(docente.puedeClasificarSolicitudes());
    }

    @Test
    void coordinadorPuedeClasificarYSerResponsable() {
        // Arrange
        Usuario coordinador = new Usuario(docCoordinador, "Ana", email, Rol.COORDINADOR);

        // Assert
        assertTrue(coordinador.puedeClasificarSolicitudes());
        assertTrue(coordinador.puedeSerResponsable());
        assertFalse(coordinador.puedeRegistrarSolicitudes());
    }

    // -------------------------------------------------------------------------
    // Igualdad por documento
    // -------------------------------------------------------------------------

    @Test
    void dosUsuariosConMismoDocumentoDeberianSerIguales() {
        // Arrange — mismo documento, distinto nombre
        Usuario u1 = new Usuario(docEstudiante, "Juan Pérez",  email, Rol.ESTUDIANTE);
        Usuario u2 = new Usuario(docEstudiante, "Juan Carlos", email, Rol.ESTUDIANTE);

        // Assert
        assertEquals(u1, u2);
        assertEquals(u1.hashCode(), u2.hashCode());
    }

    @Test
    void usuariosConDistintoDocumentoDeberianSerDiferentes() {
        // Arrange
        Usuario u1 = new Usuario(docEstudiante,  "Juan", email, Rol.ESTUDIANTE);
        Usuario u2 = new Usuario(docCoordinador, "Juan", email, Rol.COORDINADOR);

        // Assert
        assertNotEquals(u1, u2);
    }
}