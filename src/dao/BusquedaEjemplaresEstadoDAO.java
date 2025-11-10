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
        List<Ejemplar> lista = new ArrayList<>();

        String sql = "SELECT e.*, td.*, c.*, u.* " +
                "FROM ejemplar e " +
                "JOIN tipo_documento td ON e.id_tipo_documento = td.id_tipo_doc " +
                "JOIN categoria c ON e.id_categoria = c.id_categoria " +
                "JOIN ubicacion u ON e.id_ubicacion = u.id_ubicacion " +
                "WHERE e.activo = TRUE ";

        if ("Disponible".equalsIgnoreCase(estado)) {
            sql += "AND e.cantidad_disponible > 0 ";
        } else if ("Prestado".equalsIgnoreCase(estado)) {
            sql += "AND e.cantidad_disponible = 0 ";
        }

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
                lista.add(mapearEjemplar(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

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
