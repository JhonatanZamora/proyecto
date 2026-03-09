package co.edu.uniquindio.proyecto;

import co.edu.uniquindio.proyecto.domain.valueobject.DocumentoIdentidad;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoDocumento;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DocumentoIdentidadTest {

    @Test
    void deberiaCrearDocumentoCCValido() {
        // Arrange & Act
        DocumentoIdentidad doc = new DocumentoIdentidad(TipoDocumento.CC, "1094567890");

        // Assert
        assertEquals(TipoDocumento.CC, doc.tipo());
        assertEquals("1094567890", doc.numero());
    }

    @Test
    void deberiaCrearDocumentoTIValido() {
        // Arrange & Act
        DocumentoIdentidad doc = new DocumentoIdentidad(TipoDocumento.TI, "1234567890");

        // Assert
        assertEquals(TipoDocumento.TI, doc.tipo());
    }

    @Test
    void deberiaCrearDocumentoCEValido() {
        // Arrange & Act
        DocumentoIdentidad doc = new DocumentoIdentidad(TipoDocumento.CE, "CE123456");

        // Assert
        assertEquals(TipoDocumento.CE, doc.tipo());
    }

    @Test
    void deberiaCrearDocumentoPAValido() {
        // Arrange & Act
        DocumentoIdentidad doc = new DocumentoIdentidad(TipoDocumento.PA, "AB123456");

        // Assert
        assertEquals(TipoDocumento.PA, doc.tipo());
    }

    @Test
    void deberiaEliminarEspaciosDelNumero() {
        // Arrange & Act
        DocumentoIdentidad doc = new DocumentoIdentidad(TipoDocumento.CC, "  1094567890  ");

        // Assert
        assertEquals("1094567890", doc.numero());
    }

    @Test
    void deberiaLanzarErrorSiTipoEsNulo() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new DocumentoIdentidad(null, "1094567890")
        );
    }

    @Test
    void deberiaLanzarErrorSiNumeroEsVacio() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new DocumentoIdentidad(TipoDocumento.CC, "")
        );
    }

    @Test
    void deberiaLanzarErrorSiNumeroEsNulo() {
        // Arrange, Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
                new DocumentoIdentidad(TipoDocumento.CC, null)
        );
    }

    @Test
    void dosDocumentosConMismoTipoYNumeroDeberianSerIguales() {
        // Arrange
        DocumentoIdentidad d1 = new DocumentoIdentidad(TipoDocumento.CC, "1094567890");
        DocumentoIdentidad d2 = new DocumentoIdentidad(TipoDocumento.CC, "1094567890");

        // Assert
        assertEquals(d1, d2);
        assertEquals(d1.hashCode(), d2.hashCode());
    }

    @Test
    void mismoNumeroConDistintoTipoDeberianSerDiferentes() {
        // Arrange
        DocumentoIdentidad d1 = new DocumentoIdentidad(TipoDocumento.CC, "1094567890");
        DocumentoIdentidad d2 = new DocumentoIdentidad(TipoDocumento.CE, "1094567890");

        // Assert
        assertNotEquals(d1, d2);
    }
}