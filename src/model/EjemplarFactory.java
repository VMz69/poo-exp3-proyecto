package model;

/*
Factory para crear instancias de Ejemplar según el tipo
 */
public class EjemplarFactory {

    /*
     * Crea una instancia específica de Ejemplar según el nombre del tipo
     * @param tipoDocumento Nombre del tipo de documento (Libro, Tesis, CD, etc.)
     * @return Instancia de la clase correspondiente
     */
    public static Ejemplar crearEjemplar(String tipoDocumento) {
        if (tipoDocumento == null || tipoDocumento.isEmpty()) {
            throw new IllegalArgumentException("El tipo de documento no puede ser nulo o vacío");
        }

        String tipo = tipoDocumento.toUpperCase().trim();

        switch (tipo) {
            case "LIBRO":
                return new Libro();

            case "TESIS":
                return new Tesis();

            case "REVISTA":
                return new Revista();

            case "CD":
                return new CD();

            case "DVD":
                return new DVD();

            case "INFORME":
                return new Informe();

            case "MANUAL":
                return new Manual();

            default:
                // Para tipos no especificados, crear un libro genérico
                return new Libro();
        }
    }

    /**
     * Crea una instancia específica de Ejemplar desde un objeto TipoDocumento
     * @param tipoDocumento Objeto TipoDocumento
     * @return Instancia de la clase correspondiente
     */
    public static Ejemplar crearEjemplar(TipoDocumento tipoDocumento) {
        if (tipoDocumento == null) {
            throw new IllegalArgumentException("El tipo de documento no puede ser nulo");
        }
        return crearEjemplar(tipoDocumento.getNombreTipo());
    }
}