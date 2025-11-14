package model;

import java.sql.Timestamp;

public class Usuario {
    private int idUsuario;
    private String nombreCompleto;
    private String correo;
    private String usuario;
    private String contrasena;
//    private int idTipo;
    private boolean tieneMora;
    private double montoMora;
    private Timestamp fechaRegistro;
    private boolean activo;

    // Relación
    private TipoUsuario tipoUsuario;

    // Getters y Setters
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

//    public int getIdTipo() { return idTipo; }
//    public void setIdTipo(int idTipo) { this.idTipo = idTipo; }

    public boolean isTieneMora() { return tieneMora; }
    public void setTieneMora(boolean tieneMora) { this.tieneMora = tieneMora; }

    public double getMontoMora() { return montoMora; }
    public void setMontoMora(double montoMora) { this.montoMora = montoMora; }

    public Timestamp getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Timestamp fechaRegistro) { this.fechaRegistro = fechaRegistro; }  // ← CORREGIDO

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public TipoUsuario getTipoUsuario() { return tipoUsuario; }
    public void setTipoUsuario(TipoUsuario tipoUsuario) { this.tipoUsuario = tipoUsuario; }

    @Override
    public String toString() {
        return nombreCompleto + " (" + usuario + ")";
    }
}