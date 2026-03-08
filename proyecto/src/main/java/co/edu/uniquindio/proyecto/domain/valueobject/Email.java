package co.edu.uniquindio.proyecto.domain.valueobject;

import lombok.Getter;

/**
 * Value Object que representa una dirección de correo electrónico válida.
 *
 * Principio aplicado: Single Responsibility — esta clase tiene una única
 * responsabilidad: saber qué es un email válido. Nadie más en el sistema
 * necesita conocer la regla de validación de formato.
 *
 * Al ser final e inmutable, ninguna subclase puede romper estas garantías.
 */
@Getter
public final class Email {

    private static final String REGEX_EMAIL =
            "^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$";

    private final String direccion;

    /**
     * Crea un Email validado y normalizado.
     * Principio: "Un objeto que existe, es válido."
     *
     * @param direccion Dirección de correo. Se normaliza a minúsculas.
     */
    public Email(String direccion) {
        if (direccion == null || direccion.isBlank()) {
            throw new IllegalArgumentException("El email no puede estar vacío.");
        }

        String normalizado = direccion.toLowerCase().trim();

        if (!normalizado.matches(REGEX_EMAIL)) {
            throw new IllegalArgumentException(
                    "Formato de email inválido: '" + normalizado + "'."
            );
        }

        this.direccion = normalizado;
    }

    // Value Objects: igualdad por valor
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email email)) return false;
        return direccion.equals(email.direccion);
    }

    @Override
    public int hashCode() {
        return direccion.hashCode();
    }

    @Override
    public String toString() {
        return direccion;
    }
}