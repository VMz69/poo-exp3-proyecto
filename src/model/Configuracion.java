package model;
/**
 * Clase Configuracion - Configuración del sistema
 */
public class Configuracion {
    private int idConfig;
    private int maxPrestamosAlumno;
    private int maxPrestamosProfesor;
    private int diasPrestamoAlumno;
    private int diasPrestamoProfesor;
    private double moraDiaria;
    private int anioAplicacion;

//    public int getIdConfig() { return idConfig; }
    public void setIdConfig(int idConfig) { this.idConfig = idConfig; }

    public int getMaxPrestamosAlumno() { return maxPrestamosAlumno; }
    public void setMaxPrestamosAlumno(int maxPrestamosAlumno) { this.maxPrestamosAlumno = maxPrestamosAlumno; }

    public int getMaxPrestamosProfesor() { return maxPrestamosProfesor; }
    public void setMaxPrestamosProfesor(int maxPrestamosProfesor) { this.maxPrestamosProfesor = maxPrestamosProfesor; }

    public int getDiasPrestamoAlumno() { return diasPrestamoAlumno; }
    public void setDiasPrestamoAlumno(int diasPrestamoAlumno) { this.diasPrestamoAlumno = diasPrestamoAlumno; }

    public int getDiasPrestamoProfesor() { return diasPrestamoProfesor; }
    public void setDiasPrestamoProfesor(int diasPrestamoProfesor) { this.diasPrestamoProfesor = diasPrestamoProfesor; }

    public double getMoraDiaria() { return moraDiaria; }
    public void setMoraDiaria(double moraDiaria) { this.moraDiaria = moraDiaria; }

//    public int getAnioAplicacion() { return anioAplicacion; }
    public void setAnioAplicacion(int anioAplicacion) { this.anioAplicacion = anioAplicacion; }
}
