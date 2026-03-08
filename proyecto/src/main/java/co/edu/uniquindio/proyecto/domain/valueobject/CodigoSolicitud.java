package co.edu.uniquindio.proyecto.domain.valueobject;

import lombok.Getter;

import java.util.UUID;

/**
 * Value Object que representa el identificador único de una Solicitud.
 *
 * ¿Por qué no usar String directamente?
 * Porque un String puede ser cualquier cosa — un nombre, un número, un error.
 * CodigoSolicitud garantiza que siempre es un UUID válido y con significado
 * en el dominio. El compilador distingue un CodigoSolicitud de un String suelto.
 *
 * Principio aplicado: Primitive Obsession — evitar usar tipos primitivos
 * para representar conceptos del dominio.
 */
@Getter
public final class CodigoSolicitud {

    private final String valor;

    /**
     * Constructor privado — la creación se hace solo por los métodos de fábrica.
     * Nadie puede crear un CodigoSolicitud inválido desde fuera.
     */
    private CodigoSolicitud(String valor) {
        this.valor = valor;
    }

    /**
     * Genera un nuevo código único automáticamente.
     * Se usa al registrar una solicitud nueva.
     *
     * Ejemplo: CodigoSolicitud.generar()
     */
    public static CodigoSolicitud generar() {
        return new CodigoSolicitud(UUID.randomUUID().toString());
    }

    /**
     * Reconstruye un código a partir de un valor existente.
     * Se usa al recuperar una solicitud desde la base de datos.
     *
     * @param valor UUID en formato String. No puede ser nulo ni vacío.
     */
    public static CodigoSolicitud de(String valor) {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El código de solicitud no puede estar vacío.");
        }
        return new CodigoSolicitud(valor.trim());
    }

    // Value Objects se comparan por valor, nunca por referencia
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CodigoSolicitud that)) return false;
        return valor.equals(that.valor);
    }

    @Override
    public int hashCode() {
        return valor.hashCode();
    }

    @Override
    public String toString() {
        return valor;
    }
}