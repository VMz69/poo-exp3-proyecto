package model;
import java.time.LocalDate;

/**
 * Clase Base Ejemplar - Representa un documento en la biblioteca
 * Clase abstracta para implementar herencia
 */
public abstract class Ejemplar {
    private int idEjemplar;
    private String titulo;
    private String autor;
    private String editorial;
    private String isbn;
    private int anioPublicacion;
    private TipoDocumento tipoDocumento;
    private Categoria categoria;
    private Ubicacion ubicacion;
    private String numeroEdicion;
    private String idioma;
    private String descripcion;
    private int cantidadTotal;
    private int cantidadDisponible;
    private LocalDate fechaIngreso;
    private boolean activo;

    public Ejemplar() {
        this.activo = true;
    }

    // Metodo abstracto para obtener información específica del tipo
    public abstract String getInformacionEspecifica();

    // Metodo abstracto para obtener el tipo de documento
    public abstract String getTipoDocumentoString();

    // Getters y Setters
    public int getIdEjemplar() { return idEjemplar; }
    public void setIdEjemplar(int idEjemplar) { this.idEjemplar = idEjemplar; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getEditorial() { return editorial; }
    public void setEditorial(String editorial) { this.editorial = editorial; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public int getAnioPublicacion() { return anioPublicacion; }
    public void setAnioPublicacion(int anioPublicacion) { this.anioPublicacion = anioPublicacion; }

    public TipoDocumento getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(TipoDocumento tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public Ubicacion getUbicacion() { return ubicacion; }
    public void setUbicacion(Ubicacion ubicacion) { this.ubicacion = ubicacion; }

    public String getNumeroEdicion() { return numeroEdicion; }
    public void setNumeroEdicion(String numeroEdicion) { this.numeroEdicion = numeroEdicion; }

    public String getIdioma() { return idioma; }
    public void setIdioma(String idioma) { this.idioma = idioma; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getCantidadTotal() { return cantidadTotal; }
    public void setCantidadTotal(int cantidadTotal) { this.cantidadTotal = cantidadTotal; }

    public int getCantidadDisponible() { return cantidadDisponible; }
    public void setCantidadDisponible(int cantidadDisponible) { this.cantidadDisponible = cantidadDisponible; }

    public LocalDate getFechaIngreso() { return fechaIngreso; }
    public void setFechaIngreso(LocalDate fechaIngreso) { this.fechaIngreso = fechaIngreso; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    @Override
    public String toString() {
        return titulo + " - " + autor;
    }
}
