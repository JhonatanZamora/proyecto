package co.edu.uniquindio.proyecto.domain.valueobject;

import java.util.Set;

public record EstadoSolicitud(String valor) {

    private static final Set<String> ESTADOS_VALIDOS = Set.of(
            "REGISTRADA",
            "CLASIFICADA",
            "EN_ATENCION",
            "ATENDIDA",
            "CERRADA"
    );

    public EstadoSolicitud {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El estado no puede estar vacío");
        }

        if (!ESTADOS_VALIDOS.contains(valor)) {
            throw new IllegalArgumentException("Estado inválido");
        }
    }

    public boolean esCerrada() {
        return valor.equals("CERRADA");
    }
}