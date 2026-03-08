package co.edu.uniquindio.proyecto.domain.entity;

import co.edu.uniquindio.proyecto.domain.valueobject.DocumentoIdentidad;
import co.edu.uniquindio.proyecto.domain.valueobject.Email;
import co.edu.uniquindio.proyecto.domain.valueobject.Rol;
import lombok.Getter;

import java.util.Objects;

/**
 * Entidad que representa a un usuario del sistema académico.
 *
 * <p>Es una Entidad porque cada usuario tiene identidad propia definida
 * por su documento. Dos usuarios con el mismo nombre pero distinto
 * documento son personas distintas.</p>
 *
 * <p>El rol determina qué operaciones puede realizar el usuario
 * en el sistema (RF-13).</p>
 *
 * @see DocumentoIdentidad
 * @see Rol
 */
@Getter
public class Usuario {

    /** Documento de identidad — define la identidad del usuario. */
    private final DocumentoIdentidad documento;

    /** Nombre completo del usuario. */
    private final String nombreCompleto;

    /** Dirección de correo electrónico institucional. */
    private final Email email;

    /** Rol funcional dentro del sistema académico. */
    private final Rol rol;

    /**
     * Crea un nuevo usuario con todos sus datos obligatorios.
     *
     * @param documento      Documento de identidad del usuario.
     * @param nombreCompleto Nombre completo.
     * @param email          Correo electrónico institucional.
     * @param rol            Rol funcional en el sistema.
     */
    public Usuario(DocumentoIdentidad documento,
                   String nombreCompleto,
                   Email email,
                   Rol rol) {

        if (documento == null) {
            throw new IllegalArgumentException("El documento es obligatorio.");
        }
        if (nombreCompleto == null || nombreCompleto.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (email == null) {
            throw new IllegalArgumentException("El email es obligatorio.");
        }
        if (rol == null) {
            throw new IllegalArgumentException("El rol es obligatorio.");
        }

        this.documento      = documento;
        this.nombreCompleto = nombreCompleto.trim();
        this.email          = email;
        this.rol            = rol;
    }

    // -------------------------------------------------------------------------
    // Comportamientos del dominio — RF-13
    // -------------------------------------------------------------------------

    /**
     * Indica si este usuario puede registrar solicitudes académicas.
     *
     * @return {@code true} si el rol es ESTUDIANTE.
     */
    public boolean puedeRegistrarSolicitudes() {
        return this.rol == Rol.ESTUDIANTE;
    }

    /**
     * Indica si este usuario puede ser asignado como responsable.
     *
     * @return {@code true} si el rol es DOCENTE o COORDINADOR.
     */
    public boolean puedeSerResponsable() {
        return this.rol == Rol.DOCENTE || this.rol == Rol.COORDINADOR;
    }

    /**
     * Indica si este usuario puede clasificar y priorizar solicitudes.
     *
     * @return {@code true} si el rol es COORDINADOR.
     */
    public boolean puedeClasificarSolicitudes() {
        return this.rol == Rol.COORDINADOR;
    }

    // -------------------------------------------------------------------------
    // Igualdad por documento — dos usuarios son el mismo si tienen
    // el mismo tipo y número de documento
    // -------------------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario u)) return false;
        return Objects.equals(documento, u.documento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documento);
    }

    @Override
    public String toString() {
        return "Usuario{doc=" + documento.tipo() + " " + documento.numero() +
                ", nombre='" + nombreCompleto +
                "', rol=" + rol + "}";
    }
}