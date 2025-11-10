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

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        cmbEstado = new JComboBox<>(new String[]{"Disponible", "Prestado"});
        txtTitulo = new JTextField(20);
        btnBuscar = new JButton("Buscar");

        filtros.add(new JLabel("Estado:"));
        filtros.add(cmbEstado);
        filtros.add(new JLabel("Título:"));
        filtros.add(txtTitulo);
        filtros.add(btnBuscar);

        add(filtros, BorderLayout.NORTH);

        modelo = new DefaultTableModel(
                new String[]{"ID", "Título", "Autor", "Tipo", "Categoría", "Ubicación", "Total", "Disponible", "Estado"},
                0
        );

        tabla = new JTable(modelo);
        tabla.setRowHeight(25);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        btnBuscar.addActionListener(e -> ejecutarBusqueda());
    }

    private void ejecutarBusqueda() {
        String estado = (String) cmbEstado.getSelectedItem();
        String titulo = txtTitulo.getText().trim();

        List<Ejemplar> lista = new BusquedaEjemplaresEstadoDAO().buscar(estado, titulo);

        modelo.setRowCount(0);

        for (Ejemplar e : lista) {

            String ubicacion = e.getUbicacion().getEdificio() + "-" +
                    e.getUbicacion().getPiso() + "-" +
                    e.getUbicacion().getSeccion() + "-" +
                    e.getUbicacion().getEstante();

            String estadoRow = e.getCantidadDisponible() > 0 ? "Disponible" : "Prestado";

            modelo.addRow(new Object[]{
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
