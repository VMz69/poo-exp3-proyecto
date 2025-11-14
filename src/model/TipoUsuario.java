package model;

/**
 * Clase TipoUsuario - Representa el tipo de usuario
 */
public class TipoUsuario {
    private int idTipo;
    private String nombreTipo;
    private String descripcion;

    public TipoUsuario() {}

    public TipoUsuario(int idTipo, String nombreTipo) {
        this.idTipo = idTipo;
        this.nombreTipo = nombreTipo;
    }

    public int getIdTipo() { return idTipo; }
    public void setIdTipo(int idTipo) { this.idTipo = idTipo; }

    public String getNombreTipo() { return nombreTipo; }
    public void setNombreTipo(String nombreTipo) { this.nombreTipo = nombreTipo; }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        // Esto hace que cuando el objeto se muestre en un JComboBox o en un print,
        // aparezca el nombre del rol en lugar de "model.TipoUsuario@41d".
        return nombreTipo;
    }

    @Override
    public boolean equals(Object o) {
        // Si comparan el mismo objeto (misma referencia), son iguales.
        if (this == o) return true;

        // Si el objeto NO es de tipo TipoUsuario, no puede ser igual.
        if (!(o instanceof TipoUsuario)) return false;

        // Convertimos el objeto recibido al tipo correcto,
        // para poder comparar su idTipo.
        TipoUsuario that = (TipoUsuario) o;

        // Aquí definimos cuándo dos TipoUsuario se consideran iguales:
        // cuando tienen el mismo idTipo.
        //
        // Esto es esencial para que el JComboBox pueda seleccionar el rol correcto.
        return this.idTipo == that.idTipo;
    }

    @Override
    public int hashCode() {
        // hashCode debe ser consistente con equals().
        // Usamos el idTipo porque es el identificador único del rol.
        return Integer.hashCode(idTipo);
    }
}
