package co.edu.uniquindio.proyecto.domain.exception;

/**
 * Excepción base para violaciones de reglas del negocio.
 *
 * IMPORTANTE: Extiende RuntimeException (unchecked) porque las
 * violaciones del dominio son errores de lógica, no situaciones
 * recuperables que el llamador deba manejar obligatoriamente.
 *
 * Todas las excepciones del dominio deben extender esta clase,
 * no RuntimeException directamente.
 */
public class ReglaNegocioException extends RuntimeException {

    public ReglaNegocioException(String mensaje) {
        super(mensaje);
    }

    public ReglaNegocioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}