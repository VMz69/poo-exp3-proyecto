package vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import dao.BusquedaUbicacionDAO;

public class BusquedaUbicacion extends JPanel {

    // JPaneles
    JPanel panelJcombox = new JPanel();
    JPanel edificios = new JPanel();
    JPanel pisos = new JPanel();
    JPanel secciones = new JPanel();
    JPanel estantes = new JPanel();
    JPanel titulo = new JPanel();

    // componentes de los JPanel
    private JComboBox<String> comboEdificio = new JComboBox<>();
    private JComboBox<String> comboPiso = new JComboBox<>();
    private JComboBox<String> comboSeccion = new JComboBox<>();
    private JComboBox<String> comboEstante = new JComboBox<>();
    private JButton btnBuscar = new JButton("Buscar en esta ubicacion");
    private JTable tablaResultados;
    private DefaultTableModel modeloTabla;

    // instancia de la clase dao
    private BusquedaUbicacionDAO daoUbicacion = new BusquedaUbicacionDAO();

    public BusquedaUbicacion() {
        // se configura el gestor de diseño principal
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // jpanel que almacena los jCombox
        panelJcombox.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelJcombox.setMaximumSize(new Dimension(1300, 60));

        // tabla
        String[] columnas = { "Título", "Año", "Descripción", "Cantidad", "Fecha ingreso" };
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaResultados = new JTable(modeloTabla);

        // Scroll para la tabla
        JScrollPane scroll = new JScrollPane(tablaResultados);
        scroll.setBorder(new EmptyBorder(20, 0, 0, 0));
        scroll.setPreferredSize(new Dimension(1100, 400));

        // se configura gestor de diseño a los jpanel hijos de panelJcombox
        titulo.setLayout(new FlowLayout(FlowLayout.CENTER));
        titulo.setMaximumSize(new Dimension(1100, 50));
        edificios.setLayout(new BorderLayout());
        pisos.setLayout(new BorderLayout());
        secciones.setLayout(new BorderLayout());
        estantes.setLayout(new BorderLayout());

        // agregando margen x para evitar que se vea junto
        panelJcombox.setBorder(new EmptyBorder(20, 20, 0, 20));
        edificios.setBorder(new EmptyBorder(0, 10, 0, 10));
        pisos.setBorder(new EmptyBorder(0, 10, 0, 10));
        secciones.setBorder(new EmptyBorder(0, 10, 0, 10));
        estantes.setBorder(new EmptyBorder(0, 10, 0, 10));
        btnBuscar.setBackground(Color.decode("#1E90FF"));
        btnBuscar.setForeground(Color.WHITE);

        // se agrega jlabels a los hijos de jcombox
        JLabel textoEdificios = new JLabel("Selecciona un edificio: ");
        JLabel textoPiso = new JLabel("Selecciona un piso: ");
        JLabel textoSeccion = new JLabel("Selecciona una seccion: ");
        JLabel textoEstante = new JLabel("Selecciona un estante: ");
        JLabel textoTitulo = new JLabel("Busqueda por ubicacion");
        textoTitulo.setFont(new Font("Arial", Font.PLAIN, 18));
        textoTitulo.setBorder(new EmptyBorder(20, 0, 0, 0));
        titulo.add(textoTitulo);
        edificios.add(textoEdificios, BorderLayout.WEST);
        pisos.add(textoPiso, BorderLayout.WEST);
        secciones.add(textoSeccion, BorderLayout.WEST);
        estantes.add(textoEstante, BorderLayout.WEST);

        // Se obtiene lista de coordenadas de la db
        ArrayList<String> listaEdificios = daoUbicacion.obtenerDatoCoordenada("edificio");
        ArrayList<String> listaPiso = daoUbicacion.obtenerDatoCoordenada("piso");
        ArrayList<String> listaSeccion = daoUbicacion.obtenerDatoCoordenada("seccion");
        ArrayList<String> listaEstante = daoUbicacion.obtenerDatoCoordenada("estante");

        // se rellena una opcion por defecto a todos
        comboEdificio.addItem("seleccionar");
        comboPiso.addItem("seleccionar");
        comboSeccion.addItem("seleccionar");
        comboEstante.addItem("seleccionar");
        // se rellenan los jcombox
        for (String edificio : listaEdificios) {
            comboEdificio.addItem(edificio);
            edificios.add(comboEdificio, BorderLayout.EAST);
        }

        for (String piso : listaPiso) {
            comboPiso.addItem(piso);
            pisos.add(comboPiso, BorderLayout.EAST);
        }

        for (String seccion : listaSeccion) {
            comboSeccion.addItem(seccion);
            secciones.add(comboSeccion, BorderLayout.EAST);
        }

        for (String estante : listaEstante) {
            comboEstante.addItem(estante);
            estantes.add(comboEstante, BorderLayout.EAST);
        }

        // agregar listener al boton buscar y validar
        btnBuscar.addActionListener(e -> {

            // Obtener valores seleccionados de los jcombox
            String edificio = comboEdificio.getSelectedItem().toString();
            String piso = comboPiso.getSelectedItem().toString();
            String seccion = comboSeccion.getSelectedItem().toString();
            String estante = comboEstante.getSelectedItem().toString();

            // Traer datos desde la BD
            ArrayList<Object> resultados = daoUbicacion.obtenerDatosPorCoordenadas(edificio, piso, seccion, estante);

            if (comboEdificio.getSelectedIndex() == 0 || comboPiso.getSelectedIndex() == 0
                    || comboSeccion.getSelectedIndex() == 0 || comboEstante.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(null, "Debes seleccionar una ubicacion");
                return;
            } else if(resultados.size() == 0){
                JOptionPane.showMessageDialog(null, "No hay material en esa ubicacion");
            }
            // Limpiar la tabla
            modeloTabla.setRowCount(0);

            for (Object obj : resultados) {
                ArrayList<String> fila = (ArrayList<String>) obj;
                modeloTabla.addRow(fila.toArray());
            }
        });

        add(titulo);
        add(panelJcombox);
        add(scroll);
        panelJcombox.add(edificios);
        panelJcombox.add(pisos);
        panelJcombox.add(secciones);
        panelJcombox.add(estantes);
        panelJcombox.add(btnBuscar);
    }
}
