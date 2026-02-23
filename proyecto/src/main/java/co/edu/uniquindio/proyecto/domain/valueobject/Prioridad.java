package co.edu.uniquindio.proyecto.domain.valueobject;

import java.util.Set;

public record Prioridad(String nivel) {

    private static final Set<String> PRIORIDADES_VALIDAS = Set.of(
            "ALTA", "MEDIA", "BAJA"
    );

    public Prioridad {
        if (nivel == null || nivel.isBlank()) {
            throw new IllegalArgumentException("La prioridad no puede estar vacía");
        }

        if (!PRIORIDADES_VALIDAS.contains(nivel)) {
            throw new IllegalArgumentException("Prioridad inválida");
        }
    }
}