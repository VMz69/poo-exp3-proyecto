package dao;

import conexion.Conexion;
import model.Ejemplar;
import model.TipoDocumento;
import model.Categoria;
import model.Ubicacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BusquedaEjemplaresEstadoDAO {

    public List<Ejemplar> buscar(String estado, String tituloFiltro) {
        // Los parametros que recibe son el estado, que viene del dropdown, y lo que escribe el usuario, que es en base al titulo.
        List<Ejemplar> lista = new ArrayList<>(); //Inicializar array que se va a devolver

        //Declaramos el query inicial
        String sql = "SELECT e.*, td.*, c.*, u.* " +
                "FROM ejemplar e " +
                "JOIN tipo_documento td ON e.id_tipo_documento = td.id_tipo_doc " +
                "JOIN categoria c ON e.id_categoria = c.id_categoria " +
                "JOIN ubicacion u ON e.id_ubicacion = u.id_ubicacion " +
                "WHERE e.activo = TRUE ";


        //Agregamos logica: si esta disponible, agregamos al query una sentencia mas depende del estado seleccionado en el dropdown
        if ("Disponible".equalsIgnoreCase(estado)) {
            sql += "AND e.cantidad_disponible > 0 ";
        } else if ("Prestado".equalsIgnoreCase(estado)) {
            sql += "AND e.cantidad_disponible = 0 ";
        }

        // Agregamos al query lo que el usuario haya escrito para filtrar por titulo. Esto se ejecuta solo si no esta vacio. A este punto nuestro query esta completo.
        if (tituloFiltro != null && !tituloFiltro.trim().isEmpty()) {
            sql += "AND e.titulo LIKE ? ";
        }

        try (Connection conn = Conexion.conectar();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            if (tituloFiltro != null && !tituloFiltro.trim().isEmpty()) {
                pst.setString(1, "%" + tituloFiltro + "%");
            }

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                lista.add(mapearEjemplar(rs)); //Cada fila/resultado se convierte a un objeto que construye ejemplar, tipo, cat ubicacion etc
            }

        } catch (SQLException e) {
            e.printStackTrace(); //Fallback en caso falle imprimimos el stack trace
        }

        return lista; //Esto es lo que nuestra clase DAO retorna a la vista.
    }

    private Ejemplar mapearEjemplar(ResultSet rs) throws SQLException { //Este es nuestro metodo constructor de objetos y tiene todos los campos posibles. Convierte el sql en bruto a algo con lo que swing puede trabajar.
        Ejemplar e = new Ejemplar();

        // llamando los setters del modelo ejemplar, para que posteriormente pueda leerlos en el panel y no le llegue directamente el result set
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
        e.setActivo(rs.getBoolean("activo"));

        TipoDocumento td = new TipoDocumento(); // Sub objeto
        td.setIdTipoDoc(rs.getInt("id_tipo_doc"));
        td.setNombreTipo(rs.getString("nombre_tipo"));
        e.setTipoDocumento(td);

        Categoria c = new Categoria(); // Sub objeto
        c.setIdCategoria(rs.getInt("id_categoria"));
        c.setNombreCategoria(rs.getString("nombre_categoria"));
        e.setCategoria(c);

        Ubicacion u = new Ubicacion(); //Sub objetos
        u.setIdUbicacion(rs.getInt("id_ubicacion"));
        u.setEdificio(rs.getString("edificio"));
        u.setPiso(rs.getString("piso"));
        u.setSeccion(rs.getString("seccion"));
        u.setEstante(rs.getString("estante"));
        e.setUbicacion(u);

        return e; //por ahora solo necesitamos retornar el ejemplar.
    }
}
