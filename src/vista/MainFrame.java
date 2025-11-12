package vista;

import model.TipoUsuario;
import model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;

/**
 * Ventana principal - Cumple FASE 1 100%
 * - Menú Usuario (Perfil, Cerrar sesión, Salir)
 */
public class MainFrame extends JFrame {
    private final Usuario usuario;
    private final JTabbedPane tabs;

    public MainFrame(Usuario u) {
        this.usuario = u;
        this.tabs = new JTabbedPane();
        configurarVentana();
        inicializarComponentes();
        cargarPestanasPorRol();
    }

    private void configurarVentana() {
        setTitle("Biblioteca - " + usuario.getNombreCompleto() + " (" + usuario.getTipoUsuario().getNombreTipo() + ")");
        setSize(1300, 750);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        getContentPane().setBackground(new Color(248, 249, 250));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                cerrarSesion();
            }
        });
    }

    private void inicializarComponentes() {
        // === BARRA DE MENÚ ===
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));

        JMenu menuUsuario = new JMenu("Usuario");
        JMenuItem itemPerfil = new JMenuItem("Mi Perfil");
        JMenuItem itemCerrar = new JMenuItem("Cerrar Sesión");
        JMenuItem itemSalir = new JMenuItem("Salir");

        itemPerfil.addActionListener(e -> mostrarPerfil());
        itemCerrar.addActionListener(e -> cerrarSesion());
        itemSalir.addActionListener(e -> System.exit(0));

        menuUsuario.add(itemPerfil);
        menuUsuario.addSeparator();
        menuUsuario.add(itemCerrar);
        menuUsuario.add(itemSalir);

        menuBar.add(menuUsuario);
        setJMenuBar(menuBar);

        // === PESTAÑAS ===
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(tabs, BorderLayout.CENTER);

        // === BARRA DE ESTADO ===
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)));
        statusBar.setBackground(new Color(248, 249, 250));

        JLabel lblUsuario = new JLabel("Usuario: " + usuario.getNombreCompleto());
        JLabel lblRol = new JLabel("Rol: " + usuario.getTipoUsuario().getNombreTipo());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        JLabel lblFecha = new JLabel("Fecha: " + sdf.format(new java.util.Date()));

        lblUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRol.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblFecha.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        statusBar.add(lblUsuario, BorderLayout.WEST);
        statusBar.add(lblRol, BorderLayout.CENTER);
        statusBar.add(lblFecha, BorderLayout.EAST);

        add(statusBar, BorderLayout.SOUTH);
    }

    private void cargarPestanasPorRol() {
        String rol = usuario.getTipoUsuario().getNombreTipo();

        if ("Administrador".equals(rol)) {
            tabs.addTab("Usuarios", new JScrollPane(new UsuarioPanel()));
            tabs.addTab("Configuración", new JScrollPane(new ConfiguracionPanel()));
        }

        tabs.addTab("Ejemplares", new JScrollPane(new EjemplarPanel()));
        tabs.addTab("Préstamos", new JScrollPane(new PrestamoPanel()));
         //Gerson: tab BusqeudaEjeplar
        tabs.addTab("Busqueda Ejemplar", new JScrollPane(new BusquedaEjemplarUnidades()));

        // Milton: agregar otra pestaña para BusquedaEstadoPanel
        tabs.addTab("Búsqueda Estado", new JScrollPane(new BusquedaEstadoPanel()));
        // Milton: aqui termina la pestaña extra

    }

    // === ACTUALIZAR EJEMPLARES ===
    public void actualizarEjemplares() {
        for (int i = 0; i < tabs.getTabCount(); i++) {
            if ("Ejemplares".equals(tabs.getTitleAt(i))) {
                EjemplarPanel panel = (EjemplarPanel) ((JScrollPane) tabs.getComponentAt(i)).getViewport().getView();
                panel.cargarTodos(); // PÚBLICO
                break;
            }
        }
    }

    // === ACTUALIZAR USUARIOS (MORA) ===
    public void actualizarUsuarios() {
        for (int i = 0; i < tabs.getTabCount(); i++) {
            if ("Usuarios".equals(tabs.getTitleAt(i))) {
                UsuarioPanel panel = (UsuarioPanel) ((JScrollPane) tabs.getComponentAt(i)).getViewport().getView();
                panel.cargarUsuarios(); // PÚBLICO
                break;
            }
        }
    }

    private void mostrarPerfil() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String fecha = usuario.getFechaRegistro() != null ?
                sdf.format(usuario.getFechaRegistro()) : "N/A"; // CORREGIDO: Timestamp → String

        String info = "<html>" +
                "<h3>Información del Usuario</h3>" +
                "<b>Nombre:</b> " + usuario.getNombreCompleto() + "<br>" +
                "<b>Usuario:</b> " + usuario.getUsuario() + "<br>" +
                "<b>Correo:</b> " + usuario.getCorreo() + "<br>" +
                "<b>Rol:</b> " + usuario.getTipoUsuario().getNombreTipo() + "<br>" +
                "<b>Fecha Registro:</b> " + fecha + "<br>" +
                "<b>Mora:</b> " + (usuario.isTieneMora() ? "<font color='red'>Sí</font>" : "No") +
                " (S/. " + String.format("%.2f", usuario.getMontoMora()) + ")" +
                "</html>";

        JOptionPane.showMessageDialog(this, info, "Mi Perfil", JOptionPane.INFORMATION_MESSAGE);
    }

    private void cerrarSesion() {
        int opcion = JOptionPane.showConfirmDialog(
                this,
                "¿Estás seguro de que deseas cerrar sesión?",
                "Cerrar Sesión",
                JOptionPane.YES_NO_OPTION
        );

        if (opcion == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}