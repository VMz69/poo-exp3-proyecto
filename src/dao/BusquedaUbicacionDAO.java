package dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import conexion.Conexion;


public class BusquedaUbicacionDAO {

    private static final Logger log = LogManager.getLogger(BusquedaUbicacionDAO.class);
    private String sqlObtenerDatosPorCoordenada = "SELECT e.titulo, e.anio_publicaccion, e.descripcion, e.cantidad_total, e.fecha_ingreso  FROM ejemplar AS e INNER JOIN ubicacion AS u ON e.id_ubicacion = u.id_ubicacion  WHERE u.edificio = ?  AND u.piso = ?  AND u.seccion = ?  AND u.estante = ?";

    public ArrayList<String> obtenerDatoCoordenada (String dato){
        ArrayList<String> listaDatos = new ArrayList<>();
        String sqlBusqueda = "SELECT DISTINCT " + dato.toLowerCase() + " FROM ubicacion";
        try(
        Connection conn = Conexion.conectar();
        PreparedStatement consulta = conn.prepareStatement(sqlBusqueda);){
            ResultSet res = consulta.executeQuery();
            while (res.next()) {
                listaDatos.add(res.getString(dato));
            }
        } catch (SQLException e) {
            log.error("Error al insertar ejemplar", e);
        }
        return listaDatos;
    }

    public ArrayList<Object> obtenerDatosPorCoordenadas (String edificio, String piso, String seccion, String estante){
        ArrayList<Object> listadoMaterial = new ArrayList<>();
        try(
            Connection conn = Conexion.conectar();
            PreparedStatement obtenerMaterial = conn.prepareStatement(sqlObtenerDatosPorCoordenada);
        ){
            obtenerMaterial.setString(0, edificio);
            obtenerMaterial.setString(1, piso);
            obtenerMaterial.setString(2, seccion);
            obtenerMaterial.setString(3, estante);

            ResultSet res = obtenerMaterial.executeQuery();

            while (res.next()) {
                ArrayList<String>  camposMaterial = new  ArrayList<>();
                camposMaterial.add(res.getString("titulo"));
                camposMaterial.add(res.getString("anio_publicaccion"));
                camposMaterial.add(res.getString("descripcion"));
                camposMaterial.add(res.getString("cantidad_total"));
                camposMaterial.add(res.getString("fecha_ingreso"));
                listadoMaterial.add(camposMaterial);
            }
        } catch (SQLException e) {
            log.error("Error al insertar ejemplar", e);
        }
        return listadoMaterial;

    }



}
