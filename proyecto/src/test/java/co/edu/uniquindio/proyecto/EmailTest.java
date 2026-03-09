package co.edu.uniquindio.proyecto;

import co.edu.uniquindio.proyecto.domain.valueobject.Email;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void deberiaCrearEmailValido() {
        Email email = new Email("usuario@mail.com");

        assertEquals("usuario@mail.com", email.direccion());
    }

    @Test
    void deberiaNormalizarEmail() {
        Email email = new Email("  USER@MAIL.COM ");

        assertEquals("user@mail.com", email.direccion());
    }

    @Test
    void deberiaLanzarErrorSiEmailEsInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> new Email("correo_invalido"));
    }
}