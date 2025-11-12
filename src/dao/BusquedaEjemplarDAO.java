package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import conexion.Conexion;

// metodo busqueda
public class BusquedaEjemplarDAO {
    private static final Logger log = LogManager.getLogger(BusquedaEjemplarDAO.class);
    private String sqlObtenerTipoMaterial = "SELECT nombre_tipo AS tipo FROM tipo_documento";
    private String sqlObtenerListadoPorTipo = "SELECT e.titulo, e.autor, e.anio_publicacion, e.idioma, t.nombre_tipo, e.descripcion, e.fecha_ingreso, e.cantidad_total FROM ejemplar AS e INNER JOIN tipo_documento AS t ON e.id_tipo_documento = t.id_tipo_doc WHERE  LOWER(t.nombre_tipo) = LOWER(?)";

    public ArrayList<String> obtenerTipoMaterial() {
        ArrayList<String> tipos = new ArrayList<>();
        try(
        Connection conn = Conexion.conectar();
        PreparedStatement obtenerTipos = conn.prepareStatement(sqlObtenerTipoMaterial);) {
            ResultSet res = obtenerTipos.executeQuery();
            while (res.next()) {
                tipos.add(res.getString("tipo"));
            }
        } catch (SQLException e) {
            log.error("Error al insertar ejemplar", e);
        }

        return tipos;
    }

    public ArrayList<Object> obtenerListaPorTipo (String tipo){
        ArrayList<Object> lista = new ArrayList<>();
        try(
        Connection conn = Conexion.conectar();
        PreparedStatement obtennerLista = conn.prepareStatement(this.sqlObtenerListadoPorTipo);){
            obtennerLista.setString(1, tipo);
            ResultSet res = obtennerLista.executeQuery();
            while (res.next()) {
                ArrayList<String> fila = new ArrayList<>();
                fila.add(res.getString("titulo"));
                fila.add(res.getString("autor"));
                fila.add(res.getString("anio_publicacion"));
                fila.add(res.getString("idioma"));
                fila.add(res.getString("nombre_tipo"));
                fila.add(res.getString("descripcion"));
                fila.add(res.getString("fecha_ingreso"));
                fila.add(res.getString("cantidad_total"));
                lista.add(fila);
            }
        } catch (SQLException e) {
            log.error("Error al insertar ejemplar", e);
        }
        System.out.println("Registros encontrados: " + lista.size());
        return lista;
    }

}
