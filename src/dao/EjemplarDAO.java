package dao;

import conexion.Conexion;
import model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EjemplarDAO {
    private static final Logger log = LogManager.getLogger(EjemplarDAO.class);

    // === INSERTAR EJEMPLAR (CON HERENCIA) ===
    public boolean insertar(Ejemplar e) {
        String sqlBase = "INSERT INTO ejemplar (titulo, autor, editorial, isbn, anio_publicacion, " +
                "id_tipo_documento, id_categoria, id_ubicacion, numero_edicion, idioma, " +
                "num_paginas, descripcion, cantidad_total, cantidad_disponible, fecha_ingreso, activo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            conn = Conexion.conectar();
            pst = conn.prepareStatement(sqlBase, Statement.RETURN_GENERATED_KEYS);

            pst.setString(1, e.getTitulo());
            pst.setString(2, e.getAutor());
            pst.setString(3, e.getEditorial());
            pst.setString(4, e.getIsbn());
            pst.setInt(5, e.getAnioPublicacion());
            pst.setInt(6, e.getTipoDocumento().getIdTipoDoc());
            pst.setInt(7, e.getCategoria().getIdCategoria());
            pst.setInt(8, e.getUbicacion().getIdUbicacion());
            pst.setString(9, e.getNumeroEdicion());
            pst.setString(10, e.getIdioma());

            int paginas = 0;
            if (e instanceof Libro) {
                paginas = ((Libro) e).getNumPaginas();
            } else if (e instanceof Tesis) {
                paginas = ((Tesis) e).getNumPaginas();
            } else if (e instanceof Revista) {
                paginas = ((Revista) e).getNumPaginas();
            } else if (e instanceof Informe) {
                paginas = ((Informe) e).getNumPaginas();
            } else if (e instanceof Manual) {
                paginas = ((Manual) e).getNumPaginas();
            }
            pst.setInt(11, paginas);

            // Descripción: combinar descripción general con información específica
            String descripcion = (e.getDescripcion() != null ? e.getDescripcion() : "") +
                    " | " + e.getInformacionEspecifica();
            pst.setString(12, descripcion.trim());

            pst.setInt(13, e.getCantidadTotal());
            pst.setInt(14, e.getCantidadDisponible());
            pst.setObject(15, e.getFechaIngreso());
            pst.setBoolean(16, e.isActivo());

            int filas = pst.executeUpdate();
            if (filas > 0) {
                rs = pst.getGeneratedKeys();
                if (rs.next()) e.setIdEjemplar(rs.getInt(1));
                log.info("Ejemplar insertado: {} ({})", e.getTitulo(), e.getTipoDocumentoString());
                return true;
            }
        } catch (SQLException ex) {
            log.error("Error al insertar ejemplar", ex);
        } finally {
            Conexion.cerrar(rs);
            Conexion.cerrar(pst);
            Conexion.cerrar(conn);
        }
        return false;
    }

    // === OBTENER POR ID (CON HERENCIA) ===
    public Ejemplar obtenerPorId(int id) {
        String sql = "SELECT e.*, td.*, c.*, u.* " +
                "FROM ejemplar e " +
                "JOIN tipo_documento td ON e.id_tipo_documento = td.id_tipo_doc " +
                "JOIN categoria c ON e.id_categoria = c.id_categoria " +
                "JOIN ubicacion u ON e.id_ubicacion = u.id_ubicacion " +
                "WHERE e.id_ejemplar = ? AND e.activo = TRUE";

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            conn = Conexion.conectar();
            pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            rs = pst.executeQuery();
            if (rs.next()) {
                return mapearEjemplar(rs);
            }
        } catch (SQLException ex) {
            log.error("Error al obtener ejemplar por ID", ex);
        } finally {
            Conexion.cerrar(rs);
            Conexion.cerrar(pst);
            Conexion.cerrar(conn);
        }
        return null;
    }

    // === BÚSQUEDA (CON HERENCIA) ===
    public List<Ejemplar> buscar(String criterio) {
        List<Ejemplar> lista = new ArrayList<>();
        String sql = "SELECT e.*, td.*, c.*, u.* " +
                "FROM ejemplar e " +
                "JOIN tipo_documento td ON e.id_tipo_documento = td.id_tipo_doc " +
                "JOIN categoria c ON e.id_categoria = c.id_categoria " +
                "JOIN ubicacion u ON e.id_ubicacion = u.id_ubicacion " +
                "WHERE (e.titulo LIKE ? OR e.autor LIKE ? OR e.isbn LIKE ?) AND e.activo = TRUE";

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            conn = Conexion.conectar();
            pst = conn.prepareStatement(sql);
            String like = "%" + criterio + "%";
            pst.setString(1, like);
            pst.setString(2, like);
            pst.setString(3, like);
            rs = pst.executeQuery();

            while (rs.next()) {
                lista.add(mapearEjemplar(rs));
            }
        } catch (SQLException ex) {
            log.error("Error en búsqueda", ex);
        } finally {
            Conexion.cerrar(rs);
            Conexion.cerrar(pst);
            Conexion.cerrar(conn);
        }
        return lista;
    }

    // === ACTUALIZAR DISPONIBILIDAD ===
    public boolean actualizarDisponibilidad(int idEjemplar, int cambio) {
        String sql = "UPDATE ejemplar SET cantidad_disponible = cantidad_disponible + ? WHERE id_ejemplar = ? AND activo = TRUE";
        Connection conn = null;
        PreparedStatement pst = null;
        try {
            conn = Conexion.conectar();
            pst = conn.prepareStatement(sql);
            pst.setInt(1, cambio);
            pst.setInt(2, idEjemplar);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("Error al actualizar disponibilidad", e);
            return false;
        } finally {
            Conexion.cerrar(pst);
            Conexion.cerrar(conn);
        }
    }

    // === COMBOS ===
    public List<TipoDocumento> obtenerTiposDocumento() {
        return cargarCombo("tipo_documento", "id_tipo_doc", "nombre_tipo", TipoDocumento.class);
    }

    public List<Categoria> obtenerCategorias() {
        return cargarCombo("categoria", "id_categoria", "nombre_categoria", Categoria.class);
    }

    public List<Ubicacion> obtenerUbicaciones() {
        return cargarCombo("ubicacion", "id_ubicacion", "edificio", Ubicacion.class);
    }

    private <T> List<T> cargarCombo(String tabla, String idCol, String nombreCol, Class<T> clazz) {
        List<T> lista = new ArrayList<>();
        String sql = "SELECT * FROM " + tabla + " ORDER BY " + nombreCol;
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            conn = Conexion.conectar();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                if (clazz == TipoDocumento.class) {
                    TipoDocumento td = new TipoDocumento();
                    td.setIdTipoDoc(rs.getInt(idCol));
                    td.setNombreTipo(rs.getString(nombreCol));
                    lista.add((T) td);
                } else if (clazz == Categoria.class) {
                    Categoria c = new Categoria();
                    c.setIdCategoria(rs.getInt(idCol));
                    c.setNombreCategoria(rs.getString(nombreCol));
                    lista.add((T) c);
                } else if (clazz == Ubicacion.class) {
                    Ubicacion u = new Ubicacion();
                    u.setIdUbicacion(rs.getInt(idCol));
                    u.setEdificio(rs.getString("edificio"));
                    u.setPiso(rs.getString("piso"));
                    u.setSeccion(rs.getString("seccion"));
                    u.setEstante(rs.getString("estante"));
                    lista.add((T) u);
                }
            }
        } catch (SQLException e) {
            log.error("Error cargando " + tabla, e);
        } finally {
            Conexion.cerrar(rs);
            Conexion.cerrar(pst);
            Conexion.cerrar(conn);
        }
        return lista;
    }

    // === MAPEO CON HERENCIA ===
    private Ejemplar mapearEjemplar(ResultSet rs) throws SQLException {
        // Obtener tipo de documento
        TipoDocumento td = new TipoDocumento();
        td.setIdTipoDoc(rs.getInt("id_tipo_doc"));
        td.setNombreTipo(rs.getString("nombre_tipo"));

        // Crear instancia específica según el tipo
        Ejemplar e = EjemplarFactory.crearEjemplar(td);

        // Mapear campos comunes
        e.setIdEjemplar(rs.getInt("id_ejemplar"));
        e.setTitulo(rs.getString("titulo"));
        e.setAutor(rs.getString("autor"));
        e.setEditorial(rs.getString("editorial"));
        e.setIsbn(rs.getString("isbn"));
        e.setAnioPublicacion(rs.getInt("anio_publicacion"));
        e.setNumeroEdicion(rs.getString("numero_edicion"));
        e.setIdioma(rs.getString("idioma"));
        e.setDescripcion(rs.getString("descripcion"));
        e.setCantidadTotal(rs.getInt("cantidad_total"));
        e.setCantidadDisponible(rs.getInt("cantidad_disponible"));
        e.setFechaIngreso(rs.getDate("fecha_ingreso") != null ? rs.getDate("fecha_ingreso").toLocalDate() : null);
        e.setActivo(rs.getBoolean("activo"));
        e.setTipoDocumento(td);

        Categoria c = new Categoria();
        c.setIdCategoria(rs.getInt("id_categoria"));
        c.setNombreCategoria(rs.getString("nombre_categoria"));
        e.setCategoria(c);

        Ubicacion u = new Ubicacion();
        u.setIdUbicacion(rs.getInt("id_ubicacion"));
        u.setEdificio(rs.getString("edificio"));
        u.setPiso(rs.getString("piso"));
        u.setSeccion(rs.getString("seccion"));
        u.setEstante(rs.getString("estante"));
        e.setUbicacion(u);

        int paginas = rs.getInt("num_paginas");
        if (e instanceof Libro) {
            ((Libro) e).setNumPaginas(paginas);
            String desc = rs.getString("descripcion");
            if (desc != null) {
                if (desc.contains("Colección:")) {
                    String coleccion = extraerValor(desc, "Colección:");
                    ((Libro) e).setColeccion(coleccion);
                }
                if (desc.contains("Serie:")) {
                    String serie = extraerValor(desc, "Serie:");
                    ((Libro) e).setNumeroSerie(serie);
                }
            }
        } else if (e instanceof Tesis) {
            ((Tesis) e).setNumPaginas(paginas);
            String desc = rs.getString("descripcion");
            if (desc != null) {
                ((Tesis) e).setUniversidad(extraerValor(desc, "Universidad:"));
                ((Tesis) e).setFacultad(extraerValor(desc, "Facultad:"));
                ((Tesis) e).setCarrera(extraerValor(desc, "Carrera:"));
                ((Tesis) e).setAsesor(extraerValor(desc, "Asesor:"));
                ((Tesis) e).setGradoAcademico(extraerValor(desc, "Grado:"));
            }
        } else if (e instanceof Revista) {
            ((Revista) e).setNumPaginas(paginas);
            String desc = rs.getString("descripcion");
            if (desc != null) {
                ((Revista) e).setVolumen(extraerValor(desc, "Vol."));
                ((Revista) e).setNumero(extraerValor(desc, "Núm."));
                ((Revista) e).setIssn(extraerValor(desc, "ISSN:"));
                ((Revista) e).setPeriodicidad(extraerValor(desc, "Periodicidad:"));
            }
        } else if (e instanceof CD) {
            String desc = rs.getString("descripcion");
            if (desc != null) {
                String durStr = extraerValor(desc, "Duración:");
                if (durStr != null && !durStr.isEmpty()) {
                    try {
                        ((CD) e).setDuracion(Integer.parseInt(durStr.replaceAll("\\D", "")));
                    } catch (NumberFormatException ex) {}
                }
                ((CD) e).setFormato(extraerValor(desc, "Formato:"));
                ((CD) e).setArtista(extraerValor(desc, "Artista:"));
            }
        } else if (e instanceof DVD) {
            String desc = rs.getString("descripcion");
            if (desc != null) {
                String durStr = extraerValor(desc, "Duración:");
                if (durStr != null && !durStr.isEmpty()) {
                    try {
                        ((DVD) e).setDuracion(Integer.parseInt(durStr.replaceAll("\\D", "")));
                    } catch (NumberFormatException ex) {}
                }
                ((DVD) e).setFormato(extraerValor(desc, "Formato:"));
                ((DVD) e).setDirector(extraerValor(desc, "Director:"));
            }
        } else if (e instanceof Informe) {
            ((Informe) e).setNumPaginas(paginas);
            String desc = rs.getString("descripcion");
            if (desc != null) {
                ((Informe) e).setInstitucion(extraerValor(desc, "Institución:"));
                ((Informe) e).setSupervisor(extraerValor(desc, "Supervisor:"));
            }
        } else if (e instanceof Manual) {
            ((Manual) e).setNumPaginas(paginas);
            String desc = rs.getString("descripcion");
            if (desc != null) {
                ((Manual) e).setArea(extraerValor(desc, "Área:"));
                ((Manual) e).setNivelUsuario(extraerValor(desc, "Nivel:"));
                ((Manual) e).setVersion(extraerValor(desc, "Versión:"));
            }
        }

        return e;
    }

    // Metodo auxiliar para extraer valores de la descripción
    private String extraerValor(String descripcion, String clave) {
        if (descripcion == null || !descripcion.contains(clave)) {
            return "";
        }
        int inicio = descripcion.indexOf(clave) + clave.length();
        int fin = descripcion.indexOf("|", inicio);
        if (fin == -1) fin = descripcion.length();
        return descripcion.substring(inicio, fin).trim();
    }
}