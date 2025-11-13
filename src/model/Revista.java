package model;

/**
 * Clase Revista - Hereda de Ejemplar
 */
public class Revista extends Ejemplar {
    private int numPaginas;
    private String volumen;
    private String numero;
//    private String issn;
    private String periodicidad;

    public Revista() {
        super();
    }

    // Getters y Setters específicos
    public int getNumPaginas() { return numPaginas; }
    public void setNumPaginas(int numPaginas) { this.numPaginas = numPaginas; }

//    public String getVolumen() { return volumen; }
    public void setVolumen(String volumen) { this.volumen = volumen; }

//    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

//    public String getIssn() { return issn; }
//    public void setIssn(String issn) { this.issn = issn; }

//    public String getPeriodicidad() { return periodicidad; }
    public void setPeriodicidad(String periodicidad) { this.periodicidad = periodicidad; }

    @Override
    public String getInformacionEspecifica() {
        StringBuilder info = new StringBuilder();
        if (volumen != null && !volumen.isEmpty()) {
            info.append("Vol. ").append(volumen);
        }
        if (numero != null && !numero.isEmpty()) {
            if (info.length() > 0) info.append(" | ");
            info.append("Núm. ").append(numero);
        }
//        if (issn != null && !issn.isEmpty()) {
//            if (info.length() > 0) info.append(" | ");
//            info.append("ISSN: ").append(issn);
//        }
        if (periodicidad != null && !periodicidad.isEmpty()) {
            if (info.length() > 0) info.append(" | ");
            info.append("Periodicidad: ").append(periodicidad);
        }
        if (numPaginas > 0) {
            if (info.length() > 0) info.append(" | ");
            info.append("Páginas: ").append(numPaginas);
        }
        return info.toString();
    }

    @Override
    public String getTipoDocumentoString() {
        return "REVISTA";
    }

    @Override
    public String toString() {
        return super.toString() + " [Revista Vol." + volumen + " Núm." + numero + "]";
    }
}
