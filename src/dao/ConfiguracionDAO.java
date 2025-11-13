package dao;

import conexion.Conexion;
import model.Configuracion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class ConfiguracionDAO {
    private static final Logger log = LogManager.getLogger(ConfiguracionDAO.class);

    public Configuracion obtenerConfiguracion() {
        String sql = "SELECT * FROM configuracion WHERE anio_aplicacion = YEAR(CURDATE())";
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        Configuracion c = new Configuracion();
        try {
            conn = Conexion.conectar();
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            if (rs.next()) {
                c.setIdConfig(rs.getInt("id_config"));
                c.setMaxPrestamosAlumno(rs.getInt("max_prestamos_alumno"));
                c.setMaxPrestamosProfesor(rs.getInt("max_prestamos_profesor"));
                c.setDiasPrestamoAlumno(rs.getInt("dias_prestamo_alumno"));
                c.setDiasPrestamoProfesor(rs.getInt("dias_prestamo_profesor"));
                c.setMoraDiaria(rs.getDouble("mora_diaria"));
                c.setAnioAplicacion(rs.getInt("anio_aplicacion"));
            }
        } catch (SQLException e) {
            log.error("Error al obtener configuración", e);
            // valores por defecto
            c.setMaxPrestamosAlumno(3);
            c.setMaxPrestamosProfesor(5);
            c.setDiasPrestamoAlumno(7);
            c.setDiasPrestamoProfesor(14);
            c.setMoraDiaria(1.50);
            c.setAnioAplicacion(java.time.Year.now().getValue());
        } finally {
            Conexion.cerrar(rs);
            Conexion.cerrar(pst);
            Conexion.cerrar(conn);
        }
        return c;
    }

    /**
     * Actualiza o inserta la configuración del año actual
     */
    public boolean guardarConfiguracion(Configuracion c) {
        Connection conn = null;
        PreparedStatement pst = null;
        boolean exito = false;

        try {
            conn = Conexion.conectar();

            // Verificar si existe configuración para el año actual
            String sqlCheck = "SELECT id_config FROM configuracion WHERE anio_aplicacion = ?";
            pst = conn.prepareStatement(sqlCheck);
            pst.setInt(1, c.getAnioAplicacion());
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // UPDATE - Ya existe configuración
                int idConfig = rs.getInt("id_config");
                Conexion.cerrar(rs);
                Conexion.cerrar(pst);

                String sqlUpdate = "UPDATE configuracion SET " +
                        "max_prestamos_alumno = ?, " +
                        "max_prestamos_profesor = ?, " +
                        "dias_prestamo_alumno = ?, " +
                        "dias_prestamo_profesor = ?, " +
                        "mora_diaria = ? " +
                        "WHERE id_config = ?";

                pst = conn.prepareStatement(sqlUpdate);
                pst.setInt(1, c.getMaxPrestamosAlumno());
                pst.setInt(2, c.getMaxPrestamosProfesor());
                pst.setInt(3, c.getDiasPrestamoAlumno());
                pst.setInt(4, c.getDiasPrestamoProfesor());
                pst.setDouble(5, c.getMoraDiaria());
                pst.setInt(6, idConfig);

                int filas = pst.executeUpdate();
                exito = filas > 0;

                if (exito) {
                    log.info("Configuración actualizada para el año {}", c.getAnioAplicacion());
                }
            } else {
                // INSERT - No existe configuración
                Conexion.cerrar(rs);
                Conexion.cerrar(pst);

                String sqlInsert = "INSERT INTO configuracion " +
                        "(max_prestamos_alumno, max_prestamos_profesor, dias_prestamo_alumno, " +
                        "dias_prestamo_profesor, mora_diaria, anio_aplicacion) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

                pst = conn.prepareStatement(sqlInsert);
                pst.setInt(1, c.getMaxPrestamosAlumno());
                pst.setInt(2, c.getMaxPrestamosProfesor());
                pst.setInt(3, c.getDiasPrestamoAlumno());
                pst.setInt(4, c.getDiasPrestamoProfesor());
                pst.setDouble(5, c.getMoraDiaria());
                pst.setInt(6, c.getAnioAplicacion());

                int filas = pst.executeUpdate();
                exito = filas > 0;

                if (exito) {
                    log.info("Configuración creada para el año {}", c.getAnioAplicacion());
                }
            }

        } catch (SQLException e) {
            log.error("Error al guardar configuración", e);
        } finally {
            Conexion.cerrar(pst);
            Conexion.cerrar(conn);
        }
        return exito;
    }
}