package co.edu.uniquindio.proyecto;

import co.edu.uniquindio.proyecto.domain.valueobject.CodigoSolicitud;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CodigoSolicitudTest {

    @Test
    void deberiaGenerarCodigoNoNulo() {
        CodigoSolicitud codigo = CodigoSolicitud.generar();

        assertNotNull(codigo);
        assertFalse(codigo.valor().isBlank());
    }

    @Test
    void cadaCodigoGeneradoDeberiaSerUnico() {
        CodigoSolicitud c1 = CodigoSolicitud.generar();
        CodigoSolicitud c2 = CodigoSolicitud.generar();

        assertNotEquals(c1, c2);
    }

    @Test
    void deberiaReconstruirCodigoExistente() {
        CodigoSolicitud codigo = CodigoSolicitud.de("abc-123");

        assertEquals("abc-123", codigo.valor());
    }

    @Test
    void deberiaEliminarEspaciosAlReconstruir() {
        CodigoSolicitud codigo = CodigoSolicitud.de("  abc-123  ");

        assertEquals("abc-123", codigo.valor());
    }

    @Test
    void deberiaLanzarErrorSiCodigoEsVacio() {
        assertThrows(IllegalArgumentException.class,
                () -> CodigoSolicitud.de(""));
    }

    @Test
    void deberiaLanzarErrorSiCodigoEsNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> CodigoSolicitud.de(null));
    }

    @Test
    void dosCodigosConMismoValorDeberianSerIguales() {
        CodigoSolicitud c1 = CodigoSolicitud.de("abc-123");
        CodigoSolicitud c2 = CodigoSolicitud.de("abc-123");

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }
}