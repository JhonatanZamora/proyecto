package co.edu.uniquindio.proyecto.domain.valueobject;

/**
 * Documento de identidad de una persona, compuesto por tipo y número.
 *
 * <p>El tipo y el número juntos forman un concepto único del dominio.
 * Es imposible construir un {@code DocumentoIdentidad} inválido.</p>
 *
 * <p>Ejemplos de uso:</p>
 * <pre>{@code
 *   new DocumentoIdentidad(TipoDocumento.CC, "1094567890")
 *   new DocumentoIdentidad(TipoDocumento.PA, "AB123456")
 * }</pre>
 *
 * @param tipo   Tipo de documento de identidad.
 * @param numero Número del documento validado.
 */
public record DocumentoIdentidad(TipoDocumento tipo, String numero) {

    public DocumentoIdentidad {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de documento es obligatorio.");
        }
        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException("El número de documento es obligatorio.");
        }
        numero = numero.trim();
    }
}