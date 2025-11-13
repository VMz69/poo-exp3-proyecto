package model;

/**
 * Clase Informe - Hereda de Ejemplar
 */
public class Informe extends Ejemplar {
    private int numPaginas;
    private String institucion;
    private String supervisor;

    public Informe() {
        super();
    }

    // Getters y Setters específicos
    public int getNumPaginas() { return numPaginas; }
    public void setNumPaginas(int numPaginas) { this.numPaginas = numPaginas; }

//    public String getInstitucion() { return institucion; }
    public void setInstitucion(String institucion) { this.institucion = institucion; }

//    public String getSupervisor() { return supervisor; }
    public void setSupervisor(String supervisor) { this.supervisor = supervisor; }

    @Override
    public String getInformacionEspecifica() {
        StringBuilder info = new StringBuilder();
        if (institucion != null && !institucion.isEmpty()) {
            info.append("Institución: ").append(institucion);
        }
        if (supervisor != null && !supervisor.isEmpty()) {
            if (info.length() > 0) info.append(" | ");
            info.append("Supervisor: ").append(supervisor);
        }
        if (numPaginas > 0) {
            if (info.length() > 0) info.append(" | ");
            info.append("Páginas: ").append(numPaginas);
        }
        return info.toString();
    }

    @Override
    public String getTipoDocumentoString() {
        return "INFORME";
    }

    @Override
    public String toString() {
        return super.toString() + " [Informe - " + institucion + "]";
    }
}