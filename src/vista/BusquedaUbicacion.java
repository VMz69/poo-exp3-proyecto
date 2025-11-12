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
    JPanel edificios =  new JPanel();
    JPanel pisos =  new JPanel();
    JPanel secciones = new JPanel();
    JPanel estantes = new JPanel();
    JPanel titulo = new JPanel();

    //componentes de los JPanel
    private JComboBox<String> comboEdificio = new JComboBox<>();
    private JComboBox<String> comboPiso = new JComboBox<>();
    private JComboBox<String> comboSeccion = new JComboBox<>();
    private JComboBox<String> comboEstante = new JComboBox<>();
    private JButton btnBuscar = new JButton("Buscar en esta ubicacion");

    //instancia de la clase dao
    private BusquedaUbicacionDAO daoUbicacion = new BusquedaUbicacionDAO();

    public BusquedaUbicacion(){
        // se configura el gestor de diseño principal
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // jpanel que almacena los jCombox
        panelJcombox.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelJcombox.setMaximumSize(new Dimension(1100, 60));
        
        // se configura gestor de diseño a los jpanel hijos de panelJcombox
        titulo.setLayout(new FlowLayout(FlowLayout.CENTER));
        titulo.setMaximumSize(new Dimension(1100, 30));
        edificios.setLayout(new BorderLayout());
        pisos.setLayout(new BorderLayout());
        secciones.setLayout(new BorderLayout());
        estantes.setLayout(new BorderLayout());

        //agregando margen x para evitar que se vea junto
        edificios.setBorder(new EmptyBorder(10, 20, 0, 20));
        pisos.setBorder(new EmptyBorder(10, 20, 0, 20));
        secciones.setBorder(new EmptyBorder(10, 20, 0, 20));
        estantes.setBorder(new EmptyBorder(10, 20, 0, 20));
        btnBuscar.setBackground(Color.decode("#1E90FF"));
        btnBuscar.setForeground(Color.WHITE);

        // se agrega jlabels a los hijos de jcombox
        JLabel textoEdificios = new JLabel("Selecciona un edificio: ");
        JLabel textoPiso = new JLabel("Selecciona un piso: ");
        JLabel textoSeccion = new JLabel("Selecciona una seccion: ");
        JLabel textoEstante = new JLabel("Selecciona un estante: ");
        titulo.add(new JLabel("Busqueda por ubicacion"));
        edificios.add(textoEdificios, BorderLayout.WEST);
        pisos.add(textoPiso, BorderLayout.WEST);
        secciones.add(textoSeccion, BorderLayout.WEST);
        estantes.add(textoEstante, BorderLayout.WEST);

        //Se obtiene lista de coordenadas de la db
        ArrayList<String> listaEdificios = daoUbicacion.obtenerDatoCoordenada("edificio");
        ArrayList<String> listaPiso = daoUbicacion.obtenerDatoCoordenada("piso");
        ArrayList<String> listaSeccion = daoUbicacion.obtenerDatoCoordenada("seccion");
        ArrayList<String> listaEstante = daoUbicacion.obtenerDatoCoordenada("estante");

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

        add(titulo);
        add(panelJcombox);
        panelJcombox.add(edificios);
        panelJcombox.add(pisos);
        panelJcombox.add(secciones);
        panelJcombox.add(estantes);
        panelJcombox.add(btnBuscar);
    }
}
