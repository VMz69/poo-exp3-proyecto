package model;

/**
 * Clase DVD - Hereda de Ejemplar
 */
public class DVD extends Ejemplar {
    private int duracion; // en minutos
    private String formato;
    private String director;

    public DVD() {
        super();
    }

    // Getters y Setters específicos
//    public int getDuracion() { return duracion; }
    public void setDuracion(int duracion) { this.duracion = duracion; }

//    public String getFormato() { return formato; }
    public void setFormato(String formato) { this.formato = formato; }

//    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    @Override
    public String getAutorOArtista() {
        if (director != null && !director.trim().isEmpty()) {
            return director.trim();
        }
        return super.getAutorOArtista();
    }

    @Override
    public String getInformacionEspecifica() {
        StringBuilder info = new StringBuilder();
        if (duracion > 0) {
            info.append("Duración: ").append(duracion).append(" min");
        }
        if (formato != null && !formato.isEmpty()) {
            if (info.length() > 0) info.append(" | ");
            info.append("Formato: ").append(formato);
        }
        if (director != null && !director.isEmpty()) {
            if (info.length() > 0) info.append(" | ");
            info.append("Director: ").append(director);
        }
        return info.toString();
    }

    @Override
    public String getTipoDocumentoString() {
        return "DVD";
    }

    @Override
    public String toString() {
        return super.toString() + " [DVD - " + director + "]";
    }
}