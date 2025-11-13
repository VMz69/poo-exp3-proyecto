package model;

import java.sql.Date;
import java.sql.Timestamp;

public class Prestamo {
    private int idPrestamo;
    private int idUsuario;
    private int idEjemplar;
    private Timestamp fechaPrestamo;
    private Date fechaVencimiento;
    private Timestamp fechaDevolucion;
    private double moraCalculada;
    private String estado;
    private String observaciones;

    // Relaciones
    private Usuario usuario;
    private Ejemplar ejemplar;

    // ========================================
    // CONSTRUCTORES
    // ========================================
    public Prestamo() {}

//    public Prestamo(int idUsuario, int idEjemplar) {
//        this.idUsuario = idUsuario;
//        this.idEjemplar = idEjemplar;
//    }

    // ========================================
    // GETTERS Y SETTERS
    // ========================================
    public int getIdPrestamo() { return idPrestamo; }
    public void setIdPrestamo(int idPrestamo) { this.idPrestamo = idPrestamo; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }  // ← FALTABA

    public int getIdEjemplar() { return idEjemplar; }
    public void setIdEjemplar(int idEjemplar) { this.idEjemplar = idEjemplar; }  // ← FALTABA

    public Timestamp getFechaPrestamo() { return fechaPrestamo; }
    public void setFechaPrestamo(Timestamp fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }

    public Date getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(Date fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public Timestamp getFechaDevolucion() { return fechaDevolucion; }
    public void setFechaDevolucion(Timestamp fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }

    public double getMoraCalculada() { return moraCalculada; }
    public void setMoraCalculada(double moraCalculada) { this.moraCalculada = moraCalculada; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

//    public String getObservaciones() { return observaciones; }
//    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    // Relaciones
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Ejemplar getEjemplar() { return ejemplar; }
    public void setEjemplar(Ejemplar ejemplar) { this.ejemplar = ejemplar; }

    @Override
    public String toString() {
        return "Préstamo #" + idPrestamo + " - " +
                (usuario != null ? usuario.getNombreCompleto() : "ID:" + idUsuario) +
                " → " + (ejemplar != null ? ejemplar.getTitulo() : "ID:" + idEjemplar);
    }
}