package dao;

import conexion.Conexion;
import model.Ejemplar;
import model.TipoDocumento;
import model.Categoria;
import model.Ubicacion;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EjemplarDAO {
    private static final Logger log = LogManager.getLogger(EjemplarDAO.class);

    // === INSERTAR EJEMPLAR (Tesis, Libro, CD, etc.) ===
    public boolean insertar(Ejemplar e) {
        String sql = "INSERT INTO ejemplar (titulo, autor, editorial, isbn, anio_publicacion, " +
                "id_tipo_documento, id_categoria, id_ubicacion, numero_edicion, idioma, " +
                "num_paginas, descripcion, cantidad_total, cantidad_disponible, fecha_ingreso, activo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            conn = Conexion.conectar();
            pst = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

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
            pst.setInt(11, e.getNumPaginas());
            pst.setString(12, e.getDescripcion());
            pst.setInt(13, e.getCantidadTotal());
            pst.setInt(14, e.getCantidadDisponible());
            pst.setObject(15, e.getFechaIngreso());
            pst.setBoolean(16, e.isActivo());

            int filas = pst.executeUpdate();
            if (filas > 0) {
                rs = pst.getGeneratedKeys();
                if (rs.next()) e.setIdEjemplar(rs.getInt(1));
                log.info("Ejemplar insertado: {}", e.getTitulo());
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

    // === OBTENER POR ID (FALTANTE) ===
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

    // === BÚSQUEDA ===
    public List<Ejemplar> buscar(String criterio) {
        List<Ejemplar> lista = new ArrayList<>();
        String sql = "SELECT e.*, td.*, c.*, u.* " +
                "FROM ejemplar e " +
                "JOIN tipo_documento td ON e.id_tipo_documento = td.id_tipo_doc " +
                "JOIN categoria c ON e.id_categoria = c.id_categoria " +
                "JOIN ubicacion u ON e.id_ubicacion = u.id_ubicacion " +
                "WHERE e.titulo LIKE ? OR e.autor LIKE ? OR e.isbn LIKE ? AND e.activo = TRUE";

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

    // === ACTUALIZAR DISPONIBILIDAD (PRÉSTAMO/DEVOLUCIÓN) ===
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
        return cargarCombo("ubicacion", "id_ubicacion", null, Ubicacion.class);
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> cargarCombo(String tabla, String idCol, String nombreCol, Class<T> clazz) {
        List<T> lista = new ArrayList<>();
        String sql = "SELECT * FROM " + tabla + " ORDER BY " + (nombreCol != null ? nombreCol : idCol);
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

    // === MAPEO ===
    private Ejemplar mapearEjemplar(ResultSet rs) throws SQLException {
        Ejemplar e = new Ejemplar();
        e.setIdEjemplar(rs.getInt("id_ejemplar"));
        e.setTitulo(rs.getString("titulo"));
        e.setAutor(rs.getString("autor"));
        e.setEditorial(rs.getString("editorial"));
        e.setIsbn(rs.getString("isbn"));
        e.setAnioPublicacion(rs.getInt("anio_publicacion"));
        e.setNumeroEdicion(rs.getString("numero_edicion"));
        e.setIdioma(rs.getString("idioma"));
        e.setNumPaginas(rs.getInt("num_paginas"));
        e.setDescripcion(rs.getString("descripcion"));
        e.setCantidadTotal(rs.getInt("cantidad_total"));
        e.setCantidadDisponible(rs.getInt("cantidad_disponible"));
        e.setFechaIngreso(rs.getDate("fecha_ingreso") != null ? rs.getDate("fecha_ingreso").toLocalDate() : null);
        e.setActivo(rs.getBoolean("activo"));

        TipoDocumento td = new TipoDocumento();
        td.setIdTipoDoc(rs.getInt("id_tipo_doc"));
        td.setNombreTipo(rs.getString("nombre_tipo"));
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

        return e;
    }
}