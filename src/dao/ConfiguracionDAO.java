package dao;

import conexion.Conexion;
import model.Configuracion;
import java.sql.*;

public class ConfiguracionDAO {
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
}