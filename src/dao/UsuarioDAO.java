package dao;

import conexion.Conexion;
import model.Usuario;
import model.TipoUsuario;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UsuarioDAO
 * - fecha_registro → java.sql.Timestamp
 * - Métodos: autenticar, insertar, obtener, buscar, mora
 */
public class UsuarioDAO {
    private static final Logger log = LogManager.getLogger(UsuarioDAO.class);

    // ========================================
    // 1. AUTENTICAR USUARIO (LOGIN)
    // ========================================
    public Usuario autenticar(String usuario, String contrasena) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Usuario u = null;

        try {
            conn = Conexion.conectar();
            String sql = "SELECT u.*, tu.nombre_tipo FROM usuarios u " +
                    "JOIN tipo_usuario tu ON u.id_tipo = tu.id_tipo " +
                    "WHERE u.usuario = ? AND u.contrasena = ? AND u.activo = TRUE";
            ps = conn.prepareStatement(sql);
            ps.setString(1, usuario);
            ps.setString(2, contrasena);
            rs = ps.executeQuery();

            if (rs.next()) {
                u = mapearUsuario(rs);
                log.info("Usuario autenticado: {}", usuario);
            }
        } catch (SQLException e) {
            log.error("Error en autenticación", e);
        } finally {
            Conexion.cerrar(rs);
            Conexion.cerrar(ps);
            Conexion.cerrar(conn);
        }
        return u;
    }

    // ========================================
    // 2. INSERTAR USUARIO (MÓDULO ENCARGADOS)
    // ========================================
    public boolean insertar(Usuario u) {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean exito = false;

        try {
            conn = Conexion.conectar();
            String sql = "INSERT INTO usuarios (nombre_completo, correo, usuario, contrasena, id_tipo) " +
                    "VALUES (?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, u.getNombreCompleto());
            ps.setString(2, u.getCorreo());
            ps.setString(3, u.getUsuario());
            ps.setString(4, u.getContrasena());
            ps.setInt(5, u.getTipoUsuario().getIdTipo());

            int filas = ps.executeUpdate();
            exito = filas > 0;
            if (exito) {
                log.info("Usuario insertado: {}", u.getUsuario());
            }
        } catch (SQLException e) {
            log.error("Error al insertar usuario", e);
        } finally {
            Conexion.cerrar(ps);
            Conexion.cerrar(conn);
        }
        return exito;
    }

    // ========================================
    // 3. OBTENER TODOS LOS USUARIOS (TABLA)
    // ========================================
    public List<Usuario> obtenerTodos() {
        List<Usuario> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = Conexion.conectar();
            String sql = "SELECT u.*, tu.nombre_tipo FROM usuarios u " +
                    "JOIN tipo_usuario tu ON u.id_tipo = tu.id_tipo ORDER BY u.id_usuario";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            log.error("Error al obtener usuarios", e);
        } finally {
            Conexion.cerrar(rs);
            Conexion.cerrar(ps);
            Conexion.cerrar(conn);
        }
        return lista;
    }

    // ========================================
    // 4. OBTENER USUARIO POR ID
    // ========================================
    public Usuario obtenerPorId(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Usuario u = null;

        try {
            conn = Conexion.conectar();
            String sql = "SELECT u.*, tu.nombre_tipo FROM usuarios u " +
                    "JOIN tipo_usuario tu ON u.id_tipo = tu.id_tipo " +
                    "WHERE u.id_usuario = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                u = mapearUsuario(rs);
            }
        } catch (SQLException e) {
            log.error("Error al obtener usuario por ID: {}", id, e);
        } finally {
            Conexion.cerrar(rs);
            Conexion.cerrar(ps);
            Conexion.cerrar(conn);
        }
        return u;
    }

    // ========================================
    // 5. BUSCAR USUARIOS (POR NOMBRE, USUARIO O CORREO)
    // ========================================
    public List<Usuario> buscar(String criterio) {
        List<Usuario> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = Conexion.conectar();
            String sql = "SELECT u.*, tu.nombre_tipo FROM usuarios u " +
                    "JOIN tipo_usuario tu ON u.id_tipo = tu.id_tipo " +
                    "WHERE u.nombre_completo LIKE ? OR u.usuario LIKE ? OR u.correo LIKE ?";
            ps = conn.prepareStatement(sql);

            String like = "%" + criterio + "%undles";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            rs = ps.executeQuery();

            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
        } catch (SQLException e) {
            log.error("Error en búsqueda de usuarios", e);
        } finally {
            Conexion.cerrar(rs);
            Conexion.cerrar(ps);
            Conexion.cerrar(conn);
        }
        return lista;
    }

    // ========================================
    // 6. VALIDAR MORA (ANTES DE PRÉSTAMO)
    // ========================================
    public boolean tieneMora(int idUsuario) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean tiene = false;

        try {
            conn = Conexion.conectar();
            String sql = "SELECT tiene_mora FROM usuarios WHERE id_usuario = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();

            if (rs.next()) {
                tiene = rs.getBoolean("tiene_mora");
            }
        } catch (SQLException e) {
            log.error("Error al validar mora", e);
        } finally {
            Conexion.cerrar(rs);
            Conexion.cerrar(ps);
            Conexion.cerrar(conn);
        }
        return tiene;
    }

    // ========================================
    // 7. ACTUALIZAR MORA (DEVOLUCIÓN CON ATRASO)
    // ========================================
    public boolean actualizarMora(int idUsuario, boolean tieneMora, double monto) {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean exito = false;

        try {
            conn = Conexion.conectar();
            String sql = "UPDATE usuarios SET tiene_mora = ?, monto_mora = ? WHERE id_usuario = ?";
            ps = conn.prepareStatement(sql);
            ps.setBoolean(1, tieneMora);
            ps.setDouble(2, monto);
            ps.setInt(3, idUsuario);

            int filas = ps.executeUpdate();
            exito = filas > 0;
            if (exito) {
                log.info("Mora actualizada para usuario {}: {} - S/. {}", idUsuario, tieneMora, monto);
            }
        } catch (SQLException e) {
            log.error("Error al actualizar mora", e);
        } finally {
            Conexion.cerrar(ps);
            Conexion.cerrar(conn);
        }
        return exito;
    }

    // ========================================
    // 8. OBTENER TIPOS DE USUARIO (COMBO)
    // ========================================
    public List<TipoUsuario> obtenerTiposUsuario() {
        List<TipoUsuario> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = Conexion.conectar();
            String sql = "SELECT * FROM tipo_usuario ORDER BY id_tipo";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                TipoUsuario tu = new TipoUsuario();
                tu.setIdTipo(rs.getInt("id_tipo"));
                tu.setNombreTipo(rs.getString("nombre_tipo"));
                tu.setDescripcion(rs.getString("descripcion"));
                lista.add(tu);
            }
        } catch (SQLException e) {
            log.error("Error al obtener tipos de usuario", e);
        } finally {
            Conexion.cerrar(rs);
            Conexion.cerrar(ps);
            Conexion.cerrar(conn);
        }
        return lista;
    }

    // ========================================
    // MÉTODO AUXILIAR: MAPEAR RESULTSET A USUARIO
    // ========================================
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setNombreCompleto(rs.getString("nombre_completo"));
        u.setCorreo(rs.getString("correo"));
        u.setUsuario(rs.getString("usuario"));
        u.setContrasena(rs.getString("contrasena"));
        u.setTieneMora(rs.getBoolean("tiene_mora"));
        u.setMontoMora(rs.getDouble("monto_mora"));
        u.setFechaRegistro(rs.getTimestamp("fecha_registro"));  // ← CORREGIDO
        u.setActivo(rs.getBoolean("activo"));

        TipoUsuario tu = new TipoUsuario();
        tu.setIdTipo(rs.getInt("id_tipo"));
        tu.setNombreTipo(rs.getString("nombre_tipo"));
        u.setTipoUsuario(tu);

        return u;
    }
}