package vista;

import javax.swing.*;
import java.awt.*;

public class ContenedorConsultasPanel extends JPanel {

    public ContenedorConsultasPanel() {
        // 1 fila, 2 columnas -> los dos paneles se muestran en paralelo horizontal
        setLayout(new GridLayout(1, 2, 10, 0)); // 10px de separación horizontal

        add(new BusquedaUbicacion());
        add(new BusquedaEjemplarUnidades());
    }
}


