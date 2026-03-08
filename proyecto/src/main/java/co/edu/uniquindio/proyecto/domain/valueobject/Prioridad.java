package co.edu.uniquindio.proyecto.domain.valueobject;

/**
 * Nivel de urgencia de una solicitud académica.
 *
 * Es un enum porque los valores son un catálogo cerrado y conocido.
 * El compilador garantiza que nunca existirá una Prioridad inválida.
 */
public enum Prioridad {

    ALTA("Alta prioridad — atención inmediata"),
    MEDIA("Prioridad media — atención normal"),
    BAJA("Baja prioridad — atención cuando sea posible");

    // Descripción legible para mostrar en la interfaz
    private final String descripcion;

    Prioridad(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}