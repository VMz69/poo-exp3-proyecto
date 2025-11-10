package vista;

import dao.BusquedaEjemplaresEstadoDAO;
import model.Ejemplar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BusquedaEstadoPanel extends JPanel {

    private JComboBox<String> cmbEstado;
    private JTextField txtTitulo;
    private JButton btnBuscar;
    private JTable tabla;
    private DefaultTableModel modelo;

    public BusquedaEstadoPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10)); //Creamos nuestro nuevo jpanel donde va a ir el contenido

        cmbEstado = new JComboBox<>(new String[]{"Disponible", "Prestado"}); //Dropdown para que seleccionemos el estado del ejemplar que queremos buscar.
        txtTitulo = new JTextField(20); // Campo para buscar por titulo
        btnBuscar = new JButton("Buscar");

        filtros.add(new JLabel("Estado:"));  // label del dropdown
        filtros.add(cmbEstado);
        filtros.add(new JLabel("Título:")); //label del campo de texto
        filtros.add(txtTitulo);
        filtros.add(btnBuscar); // boton de buscar

        add(filtros, BorderLayout.NORTH); // aqui especifico donde es que va ir el elemento dentro de mi jpanel

        modelo = new DefaultTableModel(
                new String[]{"ID", "Título", "Autor / Artisa", "Tipo", "Categoría", "Ubicación", "Total", "Disponible", "Estado"},
                0
        ); //modelo de mi tabla que incluye las columnas que voy a mostrar

        tabla = new JTable(modelo); //Creacion de la tabla
        tabla.setRowHeight(25);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnBuscar.addActionListener(e -> ejecutarBusqueda()); //Esto es el disparador de las busquedas.
    }

    private void ejecutarBusqueda() {
        //Capturar los valores del dropdown seleccionado y el texto, removemos espacios
        String estado = (String) cmbEstado.getSelectedItem();
        String titulo = txtTitulo.getText().trim();

        List<Ejemplar> lista = new BusquedaEjemplaresEstadoDAO().buscar(estado, titulo); // Aqui llamamos el metodo y enviamos parametros. Lo que recibimos que es un array de objetos se guarda aqui.

        modelo.setRowCount(0); //limpiar por si habian rows antes

        for (Ejemplar e : lista) { //ciclo sobre todos los resultados y agregamios cada row en la jtable

            //Combinamos varios campos para retornar la ubicacion
            String ubicacion = e.getUbicacion().getEdificio() + "-" +
                    e.getUbicacion().getPiso() + "-" +
                    e.getUbicacion().getSeccion() + "-" +
                    e.getUbicacion().getEstante();

            String estadoRow = e.getCantidadDisponible() > 0 ? "Disponible" : "Prestado"; // Ternary operator que dependiendo de la cantidad define si es disponible o prestado

            modelo.addRow(new Object[]{ //Agregamos la fila completa con lo que viene de la DB  (ya convertido a objetos) mas lo que nosotros construimos aqui: ubicacion y estado
                    e.getIdEjemplar(),
                    e.getTitulo(),
                    e.getAutor(),
                    e.getTipoDocumento().getNombreTipo(),
                    e.getCategoria().getNombreCategoria(),
                    ubicacion,
                    e.getCantidadTotal(),
                    e.getCantidadDisponible(),
                    estadoRow
            });
        }
    }
}
