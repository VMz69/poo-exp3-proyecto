package dao;

import conexion.Conexion;
import model.Prestamo;
import model.Usuario;
import model.Ejemplar;
import model.TipoUsuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {
    private static final org.apache.logging.log4j.Logger log =
            org.apache.logging.log4j.LogManager.getLogger(PrestamoDAO.class);

    // ========================================
    // 1. INSERTAR PRÉSTAMO
    // ========================================
    public boolean insertar(Prestamo p) {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean exito = false;

        try {
            conn = Conexion.conectar();
            String sql = "INSERT INTO prestamo (id_usuario, id_ejemplar, fecha_vencimiento, estado) " +
                    "VALUES (?, ?, ?, 'Activo')";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, p.getIdUsuario());
            ps.setInt(2, p.getIdEjemplar());
            ps.setDate(3, p.getFechaVencimiento());

            int filas = ps.executeUpdate();
            exito = filas > 0;
            if (exito) {
                log.info("Préstamo insertado: usuario {} → ejemplar {}", p.getIdUsuario(), p.getIdEjemplar());
            }
        } catch (SQLException e) {
            log.error("Error al insertar préstamo", e);
        } finally {
            Conexion.cerrar(ps);
            Conexion.cerrar(conn);
        }
        return exito;
    }

    // ========================================
    // 2. OBTENER POR ID
    // ========================================
    public Prestamo obtenerPorId(int idPrestamo) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Prestamo p = null;

        try {
            conn = Conexion.conectar();
            String sql = "SELECT p.*, u.*, e.*, tu.nombre_tipo " +
                    "FROM prestamo p " +
                    "JOIN usuarios u ON p.id_usuario = u.id_usuario " +
                    "JOIN ejemplar e ON p.id_ejemplar = e.id_ejemplar " +
                    "JOIN tipo_usuario tu ON u.id_tipo = tu.id_tipo " +
                    "WHERE p.id_prestamo = ?";

            ps = conn.prepareStatement(sql);
            ps.setInt(1, idPrestamo);
            rs = ps.executeQuery();

            if (rs.next()) {
                p = new Prestamo();
                p.setIdPrestamo(rs.getInt("id_prestamo"));
                p.setIdUsuario(rs.getInt("id_usuario"));
                p.setIdEjemplar(rs.getInt("id_ejemplar"));
                p.setFechaPrestamo(rs.getTimestamp("fecha_prestamo"));
                p.setFechaVencimiento(rs.getDate("fecha_vencimiento"));
                p.setFechaDevolucion(rs.getTimestamp("fecha_devolucion"));
                p.setMoraCalculada(rs.getDouble("mora_calculada"));
                p.setEstado(rs.getString("estado"));

                // Usuario
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNombreCompleto(rs.getString("nombre_completo"));
                u.setCorreo(rs.getString("correo"));
                u.setUsuario(rs.getString("usuario"));
                u.setTieneMora(rs.getBoolean("tiene_mora"));
                u.setMontoMora(rs.getDouble("monto_mora"));
                u.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                u.setActivo(rs.getBoolean("activo"));

                TipoUsuario tu = new TipoUsuario();
                tu.setIdTipo(rs.getInt("id_tipo"));
                tu.setNombreTipo(rs.getString("nombre_tipo"));
                u.setTipoUsuario(tu);
                p.setUsuario(u);

                // Ejemplar
                Ejemplar e = new Ejemplar();
                e.setIdEjemplar(rs.getInt("id_ejemplar"));
                e.setTitulo(rs.getString("titulo"));
                e.setAutor(rs.getString("autor"));
                e.setCantidadDisponible(rs.getInt("cantidad_disponible"));
                e.setCantidadTotal(rs.getInt("cantidad_total"));
                p.setEjemplar(e);
            }
        } catch (SQLException e) {
            log.error("Error al obtener préstamo por ID: {}", idPrestamo, e);
        } finally {
            Conexion.cerrar(rs);
            Conexion.cerrar(ps);
            Conexion.cerrar(conn);
        }
        return p;
    }

    // ========================================
    // 3. OBTENER PRÉSTAMOS ACTIVOS
    // ========================================
    public List<Prestamo> obtenerActivos() {
        List<Prestamo> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = Conexion.conectar();
            String sql = "SELECT p.*, u.nombre_completo, e.titulo " +
                    "FROM prestamo p " +
                    "JOIN usuarios u ON p.id_usuario = u.id_usuario " +
                    "JOIN ejemplar e ON p.id_ejemplar = e.id_ejemplar " +
                    "WHERE p.estado = 'Activo' ORDER BY p.fecha_prestamo DESC";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Prestamo p = new Prestamo();
                p.setIdPrestamo(rs.getInt("id_prestamo"));
                p.setIdUsuario(rs.getInt("id_usuario"));
                p.setIdEjemplar(rs.getInt("id_ejemplar"));
                p.setFechaPrestamo(rs.getTimestamp("fecha_prestamo"));
                p.setFechaVencimiento(rs.getDate("fecha_vencimiento"));
                p.setFechaDevolucion(rs.getTimestamp("fecha_devolucion"));
                p.setMoraCalculada(rs.getDouble("mora_calculada"));
                p.setEstado(rs.getString("estado"));

                // Relaciones básicas
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNombreCompleto(rs.getString("nombre_completo"));
                p.setUsuario(u);

                Ejemplar e = new Ejemplar();
                e.setIdEjemplar(rs.getInt("id_ejemplar"));
                e.setTitulo(rs.getString("titulo"));
                p.setEjemplar(e);

                lista.add(p);
            }
        } catch (SQLException e) {
            log.error("Error al obtener préstamos activos", e);
        } finally {
            Conexion.cerrar(rs);
            Conexion.cerrar(ps);
            Conexion.cerrar(conn);
        }
        return lista;
    }

    // ========================================
    // 4. ACTUALIZAR PRÉSTAMO (DEVOLUCIÓN)
    // ========================================
    public boolean actualizar(Prestamo p) {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean exito = false;

        try {
            conn = Conexion.conectar();
            String sql = "UPDATE prestamo SET fecha_devolucion = ?, mora_calculada = ?, estado = ? " +
                    "WHERE id_prestamo = ?";
            ps = conn.prepareStatement(sql);
            ps.setTimestamp(1, p.getFechaDevolucion());
            ps.setDouble(2, p.getMoraCalculada());
            ps.setString(3, p.getEstado());
            ps.setInt(4, p.getIdPrestamo());

            int filas = ps.executeUpdate();
            exito = filas > 0;
            if (exito) {
                log.info("Préstamo devuelto: ID {}", p.getIdPrestamo());
            }
        } catch (SQLException e) {
            log.error("Error al actualizar préstamo", e);
        } finally {
            Conexion.cerrar(ps);
            Conexion.cerrar(conn);
        }
        return exito;
    }

    // ========================================
    // MÉTODO AUXILIAR: MAPEAR PRÉSTAMO COMPLETO
    // ========================================
    private Prestamo mapearPrestamoCompleto(ResultSet rs) throws SQLException {
        Prestamo p = new Prestamo();
        p.setIdPrestamo(rs.getInt("id_prestamo"));
        p.setIdUsuario(rs.getInt("id_usuario"));
        p.setIdEjemplar(rs.getInt("id_ejemplar"));
        p.setFechaPrestamo(rs.getTimestamp("fecha_prestamo"));
        p.setFechaVencimiento(rs.getDate("fecha_vencimiento"));
        p.setFechaDevolucion(rs.getTimestamp("fecha_devolucion"));
        p.setMoraCalculada(rs.getDouble("mora_calculada"));
        p.setEstado(rs.getString("estado"));

        // Usuario completo
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setNombreCompleto(rs.getString("nombre_completo"));
        u.setCorreo(rs.getString("correo"));
        u.setUsuario(rs.getString("usuario"));
        u.setTieneMora(rs.getBoolean("tiene_mora"));
        u.setMontoMora(rs.getDouble("monto_mora"));

        TipoUsuario tu = new TipoUsuario();
        tu.setIdTipo(rs.getInt("id_tipo"));
        tu.setNombreTipo(rs.getString("nombre_tipo"));
        u.setTipoUsuario(tu);
        p.setUsuario(u);

        // Ejemplar completo
        Ejemplar e = new Ejemplar();
        e.setIdEjemplar(rs.getInt("id_ejemplar"));
        e.setTitulo(rs.getString("titulo"));
        e.setAutor(rs.getString("autor"));
        e.setCantidadDisponible(rs.getInt("cantidad_disponible"));
        e.setCantidadTotal(rs.getInt("cantidad_total"));

        // Ubicación
        String ubicacion = rs.getString("edificio") + "-" +
                rs.getString("piso") + "-" +
                rs.getString("seccion") + "-" +
                rs.getString("estante");
        // (puedes crear un objeto Ubicacion si lo necesitas)

        p.setEjemplar(e);
        return p;
    }
}