package co.edu.uniquindio.proyecto.domain.valueobject;

import java.util.Set;

public record TipoSolicitud(String valor) {

    private static final Set<String> TIPOS_VALIDOS = Set.of(
            "HOMOLOGACION",
            "REGISTRO_ASIGNATURA",
            "CANCELACION",
            "CUPO",
            "CONSULTA_ACADEMICA"
    );

    public TipoSolicitud {
        if (valor == null || valor.isBlank()) {
            throw new IllegalArgumentException("El tipo de solicitud no puede estar vacío");
        }

        if (!TIPOS_VALIDOS.contains(valor)) {
            throw new IllegalArgumentException("Tipo de solicitud inválido");
        }
    }
}