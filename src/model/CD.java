package model;

/**
 * Clase CD - Hereda de Ejemplar
 */
public class CD extends Ejemplar {
    private int duracion; // en minutos
    private String formato;
    private String artista;

    public CD() {
        super();
    }

    // Getters y Setters específicos
//    public int getDuracion() { return duracion; }
    public void setDuracion(int duracion) { this.duracion = duracion; }

//    public String getFormato() { return formato; }
    public void setFormato(String formato) { this.formato = formato; }

//    public String getArtista() { return artista; }
    public void setArtista(String artista) { this.artista = artista; }

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
        if (artista != null && !artista.isEmpty()) {
            if (info.length() > 0) info.append(" | ");
            info.append("Artista: ").append(artista);
        }
        return info.toString();
    }

    @Override
    public String getTipoDocumentoString() {
        return "CD";
    }

    @Override
    public String toString() {
        return super.toString() + " [CD - " + artista + "]";
    }
}