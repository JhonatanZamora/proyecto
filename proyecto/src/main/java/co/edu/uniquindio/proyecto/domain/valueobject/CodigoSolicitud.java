package co.edu.uniquindio.proyecto.domain.valueobject;

import java.util.UUID;

/**
 * Identificador único e inmutable de una solicitud académica.
 *
 * <p>Encapsula la generación y validación del código, evitando que
 * otras capas manipulen cadenas sin significado explícito.</p>
 *
 * <p>Ejemplos de uso:</p>
 * <pre>{@code
 *   CodigoSolicitud nuevo     = CodigoSolicitud.generar();
 *   CodigoSolicitud existente = CodigoSolicitud.de("550e8400-e29b-...");
 *
 *   nuevo.valor(); // → "550e8400-e29b-41d4-a716-446655440000"
 * }</pre>
 *
 * @param valor Cadena no vacía que representa el código único.
 */
public record CodigoSolicitud(String valor) {

    /**
     * Constructor compacto — valida que el valor no esté vacío y elimina espacios.
     */
    public CodigoSolicitud {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El código no puede estar vacío.");
        }
        valor = valor.trim();
    }

    /**
     * Crea un nuevo código generando un UUID aleatorio.
     *
     * <p>Úsalo cuando se registra una nueva solicitud.</p>
     *
     * @return Nueva instancia con un UUID único como valor.
     */
    public static CodigoSolicitud generar() {
        return new CodigoSolicitud(UUID.randomUUID().toString());
    }

    /**
     * Reconstruye un código a partir de un valor ya existente.
     *
     * <p>Úsalo cuando se carga una solicitud desde la base de datos.</p>
     *
     * @param valor Cadena del código existente.
     * @return Instancia que representa el código existente.
     * @throws IllegalArgumentException si el valor es nulo o vacío.
     */
    public static CodigoSolicitud de(String valor) {
        return new CodigoSolicitud(valor);
    }
}
