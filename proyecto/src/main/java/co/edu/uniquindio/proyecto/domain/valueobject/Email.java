package co.edu.uniquindio.proyecto.domain.valueobject;

public record Email(String direccion) {

    private static final String REGEX_EMAIL =
            "^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$";

    public Email {
        if (direccion == null || direccion.isBlank()) {
            throw new IllegalArgumentException("El email no puede estar vacío.");
        }
        direccion = direccion.toLowerCase().trim();
        if (!direccion.matches(REGEX_EMAIL)) {
            throw new IllegalArgumentException(
                    "Formato de email inválido: '" + direccion + "'."
            );
        }
    }
}