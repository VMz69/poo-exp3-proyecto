package model;

/**
 * Clase Tesis - Hereda de Ejemplar
 */
public class Tesis extends Ejemplar {
    private int numPaginas;
    private String universidad;
    private String facultad;
    private String carrera;
    private String asesor;
    private String gradoAcademico;

    public Tesis() {
        super();
    }

    // Getters y Setters específicos
    public int getNumPaginas() { return numPaginas; }
    public void setNumPaginas(int numPaginas) { this.numPaginas = numPaginas; }

    public String getUniversidad() { return universidad; }
    public void setUniversidad(String universidad) { this.universidad = universidad; }

    public String getFacultad() { return facultad; }
    public void setFacultad(String facultad) { this.facultad = facultad; }

    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }

    public String getAsesor() { return asesor; }
    public void setAsesor(String asesor) { this.asesor = asesor; }

    public String getGradoAcademico() { return gradoAcademico; }
    public void setGradoAcademico(String gradoAcademico) { this.gradoAcademico = gradoAcademico; }

    @Override
    public String getInformacionEspecifica() {
        StringBuilder info = new StringBuilder();
        if (universidad != null && !universidad.isEmpty()) {
            info.append("Universidad: ").append(universidad);
        }
        if (facultad != null && !facultad.isEmpty()) {
            if (info.length() > 0) info.append(" | ");
            info.append("Facultad: ").append(facultad);
        }
        if (carrera != null && !carrera.isEmpty()) {
            if (info.length() > 0) info.append(" | ");
            info.append("Carrera: ").append(carrera);
        }
        if (asesor != null && !asesor.isEmpty()) {
            if (info.length() > 0) info.append(" | ");
            info.append("Asesor: ").append(asesor);
        }
        if (gradoAcademico != null && !gradoAcademico.isEmpty()) {
            if (info.length() > 0) info.append(" | ");
            info.append("Grado: ").append(gradoAcademico);
        }
        if (numPaginas > 0) {
            if (info.length() > 0) info.append(" | ");
            info.append("Páginas: ").append(numPaginas);
        }
        return info.toString();
    }

    @Override
    public String getTipoDocumentoString() {
        return "TESIS";
    }

    @Override
    public String toString() {
        return super.toString() + " [Tesis - " + gradoAcademico + "]";
    }
}