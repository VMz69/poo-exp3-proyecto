// model/Manual.java
package model;

public class Manual extends Ejemplar {
    private String area;
    private String nivelUsuario;
    private String version;
    private int numPaginas;  // AÑADE ESTO

    public Manual() {}

    @Override
    public String getInformacionEspecifica() {
        return "Área: " + (area != null ? area : "N/A") +
                " | Nivel: " + (nivelUsuario != null ? nivelUsuario : "N/A") +
                " | Versión: " + (version != null ? version : "N/A") +
                " | Páginas: " + (numPaginas > 0 ? numPaginas : "N/A");
    }

    @Override
    public String getTipoDocumentoString() {
        return "MANUAL";
    }

    // Getters y Setters
    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }
    public String getNivelUsuario() { return nivelUsuario; }
    public void setNivelUsuario(String nivelUsuario) { this.nivelUsuario = nivelUsuario; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public int getNumPaginas() { return numPaginas; }
    public void setNumPaginas(int numPaginas) { this.numPaginas = numPaginas; }
}