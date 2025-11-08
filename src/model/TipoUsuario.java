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

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return nombreTipo;
    }
}