package model;

/**
 * Clase Libro - Hereda de Ejemplar
 */
public class Libro extends Ejemplar {
    private int numPaginas;
    private String coleccion;
    private String numeroSerie;

    public Libro() {
        super();
    }

    // Getters y Setters específicos
    public int getNumPaginas() { return numPaginas; }
    public void setNumPaginas(int numPaginas) { this.numPaginas = numPaginas; }

//    public String getColeccion() { return coleccion; }
    public void setColeccion(String coleccion) { this.coleccion = coleccion; }

//    public String getNumeroSerie() { return numeroSerie; }
    public void setNumeroSerie(String numeroSerie) { this.numeroSerie = numeroSerie; }

    @Override
    public String getInformacionEspecifica() {
        StringBuilder info = new StringBuilder();
        if (numPaginas > 0) {
            info.append("Páginas: ").append(numPaginas);
        }
        if (coleccion != null && !coleccion.isEmpty()) {
            if (info.length() > 0) info.append(" | ");
            info.append("Colección: ").append(coleccion);
        }
        if (numeroSerie != null && !numeroSerie.isEmpty()) {
            if (info.length() > 0) info.append(" | ");
            info.append("Serie: ").append(numeroSerie);
        }
        return info.toString();
    }

    @Override
    public String getTipoDocumentoString() {
        return "LIBRO";
    }

    @Override
    public String toString() {
        return super.toString() + " [Libro - " + numPaginas + " págs.]";
    }
}