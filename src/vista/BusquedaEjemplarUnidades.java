package vista;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import dao.BusquedaEjemplarDAO;

public class BusquedaEjemplarUnidades extends JPanel {

    private BusquedaEjemplarDAO ejemplarBusq = new BusquedaEjemplarDAO();
    private ArrayList<String> tipos = this.ejemplarBusq.obtenerTipoMaterial();

    private JPanel tituloPrincipal = new JPanel();
    private JPanel menu = new JPanel();
    private JPanel panelResultados = new JPanel();
    private JPanel panelConteoTipos = new JPanel();

    // componentes de los JPanel
    private JLabel titulo = new JLabel();
    private JLabel cantidadTipo = new JLabel();
    private int obtenerCantidad = 0;

    public BusquedaEjemplarUnidades() {
        // gestor de layout del main
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Panel conteo de tipo y nombre de titulo
        panelConteoTipos.setLayout(new BorderLayout());
        panelConteoTipos.add(cantidadTipo, BorderLayout.CENTER);
        titulo.setText("Libros");
        //Panel de titulo
        tituloPrincipal.setLayout(new BorderLayout());
        tituloPrincipal.setBackground(Color.WHITE);
        tituloPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        tituloPrincipal.add(titulo, BorderLayout.WEST);
        tituloPrincipal.add(panelConteoTipos, BorderLayout.EAST);

        // Panel de menu botones 
        menu.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        for (String tipo : tipos) {
            JButton btn = estilizarBoton(new JButton("<html><u>" + tipo + "</u></html>"));
            menu.add(btn);
            btn.addActionListener(e -> {
                cargarTablaPorTipo(tipo);
                titulo.setText(tipo);
            });
        }

        // Panel de resultados
        panelResultados.setLayout(new BorderLayout());
        panelResultados.setPreferredSize(new Dimension(800, 640));

        // Crear tabla 
        String[] columnas = { "TITULO", "AUTOR/ARTISTA", "AÑO", "IDIOMA", "TIPO", "DESCRIPCION", "INGRESO", "CANTIDAD" };
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modelo);
        tabla.setFillsViewportHeight(true);
        JScrollPane scroll = new JScrollPane(tabla);
        panelResultados.add(scroll, BorderLayout.CENTER);

        ArrayList<Object> lista = ejemplarBusq.obtenerListaPorTipo("Libro");
        for (Object filaObj : lista) {
            ArrayList<?> fila = (ArrayList<?>) filaObj;
            modelo.addRow(new Object[] {
                    fila.get(0), 
                    fila.get(1), 
                    fila.get(2), 
                    fila.get(3), 
                    fila.get(4), 
                    fila.get(5), 
                    fila.get(6),
                    fila.get(7)
            });
            
            obtenerCantidad += Integer.parseInt(String.valueOf(fila.get(7)));
        }
        cantidadTipo.setText(String.valueOf(obtenerCantidad));

        // Agregar paneles al principal
        add(menu);
        add(tituloPrincipal);
        add(panelResultados);
    }

    // Metodo para recargar la tabla según el tipo
    private void cargarTablaPorTipo(String tipo) {
        
        JScrollPane scroll = (JScrollPane) panelResultados.getComponent(0);
        JTable tabla = (JTable) scroll.getViewport().getView();
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        obtenerCantidad = 0;
        modelo.setRowCount(0); 

        ArrayList<Object> lista = ejemplarBusq.obtenerListaPorTipo(tipo);
        for (Object filaObj : lista) {
            ArrayList<?> fila = (ArrayList<?>) filaObj;
            modelo.addRow(new Object[] {
                    fila.get(0), 
                    fila.get(1), 
                    fila.get(2), 
                    fila.get(3), 
                    fila.get(4), 
                    fila.get(5), 
                    fila.get(6),
                    fila.get(7)
            });
            
            obtenerCantidad += Integer.parseInt(String.valueOf(fila.get(7)));
        }
        cantidadTipo.setText(String.valueOf(obtenerCantidad));
    }

    private JButton estilizarBoton(JButton boton) {
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setFont(new Font("Cambria", Font.BOLD, 14));
        boton.setForeground(Color.decode("#0078D7"));
        return boton;
    }
}
