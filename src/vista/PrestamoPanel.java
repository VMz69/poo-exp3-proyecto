package vista;

import dao.EjemplarDAO;
import dao.PrestamoDAO;
import dao.UsuarioDAO;
import dao.ConfiguracionDAO;
import model.Ejemplar;
import model.Prestamo;
import model.Usuario;
import model.Configuracion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Panel de Préstamos
 */
public class PrestamoPanel extends JPanel {
    private JTextField txtIdUsuario, txtIdEjemplar;
    private JButton btnBuscarUsuario, btnBuscarEjemplar, btnPrestar, btnDevolver;
    private JTable tabla;
    private DefaultTableModel modelo;

    public PrestamoPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // === TÍTULO ===
        JLabel titulo = new JLabel("Gestión de Préstamos", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(titulo, BorderLayout.NORTH);

        // === FORMULARIO ===
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtIdUsuario = new JTextField(10);
        txtIdEjemplar = new JTextField(10);

        btnBuscarUsuario = new JButton("Confirmar Usuario");
        btnBuscarEjemplar = new JButton("Confirmar Ejemplar");
        btnPrestar = new JButton("Realizar Préstamo");
        btnPrestar.setBackground(new Color(40, 167, 69));
        btnPrestar.setForeground(Color.WHITE);

        int y = 0;
        addRow(form, gbc, "ID Usuario:", txtIdUsuario, btnBuscarUsuario, y++);
        addRow(form, gbc, "ID Ejemplar:", txtIdEjemplar, btnBuscarEjemplar, y++);
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 3;
        form.add(btnPrestar, gbc);

        add(form, BorderLayout.WEST);

        // === TABLA ===
        String[] columnas = {"ID", "Usuario", "Ejemplar", "Préstamo", "Vencimiento", "Estado"};
        modelo = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.setRowHeight(25);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // === BOTÓN DEVOLVER ===
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnDevolver = new JButton("Devolver Seleccionado");
        btnDevolver.setBackground(new Color(220, 53, 69));
        btnDevolver.setForeground(Color.WHITE);
        panelSur.add(btnDevolver);
        add(panelSur, BorderLayout.SOUTH);

        // === ACCIONES ===
        btnBuscarUsuario.addActionListener(e -> buscarUsuario());
        btnBuscarEjemplar.addActionListener(e -> buscarEjemplar());
        btnPrestar.addActionListener(e -> hacerPrestamo());
        btnDevolver.addActionListener(e -> devolverPrestamo());

        cargarPrestamos();
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, String label, JTextField field, JButton button, int y) {
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
        gbc.gridx = 2;
        panel.add(button, gbc);
    }

    private void buscarUsuario() {
        String id = txtIdUsuario.getText().trim();
        if (id.isEmpty()) return;

        try {
            Usuario u = new UsuarioDAO().obtenerPorId(Integer.parseInt(id));
            if (u != null) {
                // Obtener configuración actual para mostrar límites
                ConfiguracionDAO cdao = new ConfiguracionDAO();
                PrestamoDAO pdao = new PrestamoDAO();
                Configuracion config = cdao.obtenerConfiguracion();

                int prestamosActivos = pdao.contarPrestamosActivos(u.getIdUsuario());
                int limiteMax = u.getTipoUsuario().getNombreTipo().equalsIgnoreCase("Profesor")
                        ? config.getMaxPrestamosProfesor()
                        : config.getMaxPrestamosAlumno();

                int diasPrestamo = u.getTipoUsuario().getNombreTipo().equalsIgnoreCase("Profesor")
                        ? config.getDiasPrestamoProfesor()
                        : config.getDiasPrestamoAlumno();

                JOptionPane.showMessageDialog(this,
                        "<html><b>Usuario encontrado:</b><br>" +
                                u.getNombreCompleto() + " (" + u.getTipoUsuario().getNombreTipo() + ")<br><br>" +
                                "<b>Préstamos activos:</b> " + prestamosActivos + " / " + limiteMax + "<br>" +
                                "<b>Días de préstamo:</b> " + diasPrestamo + " días<br><br>" +
                                (u.isTieneMora() ? "<font color='red'><b>Mora: S/. " + String.format("%.2f", u.getMontoMora()) + "</b></font>" : "<font color='green'>Sin mora</font>") +
                                "</html>", "Usuario", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID válido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarEjemplar() {
        String id = txtIdEjemplar.getText().trim();
        if (id.isEmpty()) return;

        try {
            Ejemplar e = new EjemplarDAO().obtenerPorId(Integer.parseInt(id));
            if (e != null && e.getCantidadDisponible() > 0) {
                JOptionPane.showMessageDialog(this,
                        "<html><b>Ejemplar disponible:</b><br>" +
                                e.getTitulo() + " - " + e.getAutor() + "<br>" +
                                "Disponible: " + e.getCantidadDisponible() + " / " + e.getCantidadTotal() +
                                "</html>", "Ejemplar", JOptionPane.INFORMATION_MESSAGE);
            } else if (e != null) {
                JOptionPane.showMessageDialog(this,
                        "Ejemplar encontrado pero sin unidades disponibles.\n" +
                                "Total: " + e.getCantidadTotal() + " | Disponible: " + e.getCantidadDisponible(),
                        "No disponible", JOptionPane.WARNING_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Ejemplar no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID válido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hacerPrestamo() {
        try {
            int idUsuario = Integer.parseInt(txtIdUsuario.getText().trim());
            int idEjemplar = Integer.parseInt(txtIdEjemplar.getText().trim());

            UsuarioDAO udao = new UsuarioDAO();
            EjemplarDAO edao = new EjemplarDAO();
            PrestamoDAO pdao = new PrestamoDAO();
            ConfiguracionDAO cdao = new ConfiguracionDAO();

            // Obtener configuracion actual
            Configuracion config = cdao.obtenerConfiguracion();

            // Obtener usuario
            Usuario u = udao.obtenerPorId(idUsuario);
            if (u == null) {
                JOptionPane.showMessageDialog(this, "Usuario no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validacion de la mora
            if (u.isTieneMora()) {
                JOptionPane.showMessageDialog(this,
                        "<html><b>Usuario con mora pendiente</b><br><br>" +
                                "Monto: S/. " + String.format("%.2f", u.getMontoMora()) + "<br><br>" +
                                "El usuario debe pagar su mora antes de realizar nuevos préstamos." +
                                "</html>",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validacion limite de prestamos
            int prestamosActivos = pdao.contarPrestamosActivos(idUsuario);

            int limiteMax = u.getTipoUsuario().getNombreTipo().equalsIgnoreCase("Profesor")
                    ? config.getMaxPrestamosProfesor()
                    : config.getMaxPrestamosAlumno();

            if (prestamosActivos >= limiteMax) {
                JOptionPane.showMessageDialog(this,
                        "<html><b>Límite de préstamos alcanzado</b><br><br>" +
                                "Usuario: " + u.getNombreCompleto() + " (" + u.getTipoUsuario().getNombreTipo() + ")<br>" +
                                "Préstamos activos: " + prestamosActivos + " / " + limiteMax + "<br><br>" +
                                "Debe devolver algún ejemplar antes de realizar un nuevo préstamo." +
                                "</html>",
                        "Límite alcanzado", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validacion disponibilidad
            Ejemplar e = edao.obtenerPorId(idEjemplar);
            if (e == null || e.getCantidadDisponible() <= 0) {
                JOptionPane.showMessageDialog(this, "Ejemplar no disponible.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Calcular dias de prestamo
            int dias = u.getTipoUsuario().getNombreTipo().equalsIgnoreCase("Profesor")
                    ? config.getDiasPrestamoProfesor()
                    : config.getDiasPrestamoAlumno();

            LocalDate vencimiento = LocalDate.now().plusDays(dias);

            // Crear prestamo
            Prestamo p = new Prestamo();
            p.setIdUsuario(idUsuario);
            p.setIdEjemplar(idEjemplar);
            p.setFechaVencimiento(java.sql.Date.valueOf(vencimiento));

            if (pdao.insertar(p)) {
                edao.actualizarDisponibilidad(idEjemplar, -1);
                JOptionPane.showMessageDialog(this,
                        "<html><b>Préstamo realizado con éxito</b><br><br>" +
                                "<b>Usuario:</b> " + u.getNombreCompleto() + "<br>" +
                                "<b>Tipo:</b> " + u.getTipoUsuario().getNombreTipo() + "<br>" +
                                "<b>Ejemplar:</b> " + e.getTitulo() + "<br>" +
                                "<b>Días de préstamo:</b> " + dias + " días<br>" +
                                "<b>Vencimiento:</b> " + vencimiento + "<br><br>" +
                                "<b>Préstamos activos:</b> " + (prestamosActivos + 1) + " / " + limiteMax +
                                "</html>",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarPrestamos();

                MainFrame main = (MainFrame) SwingUtilities.getWindowAncestor(this);
                if (main != null) {
                    main.actualizarEjemplares();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al registrar el préstamo en la base de datos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese IDs válidos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void devolverPrestamo() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un préstamo de la tabla", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idPrestamo = (int) modelo.getValueAt(fila, 0);
        PrestamoDAO pdao = new PrestamoDAO();
        ConfiguracionDAO cdao = new ConfiguracionDAO();
        Prestamo p = pdao.obtenerPorId(idPrestamo);

        if (p != null && p.getFechaDevolucion() == null) {
            // Usar mora diaria de configuracion actual
            Configuracion config = cdao.obtenerConfiguracion();
            double moraDiaria = config.getMoraDiaria();

            long diasAtraso = java.time.temporal.ChronoUnit.DAYS.between(
                    p.getFechaVencimiento().toLocalDate(), LocalDate.now());
            double mora = diasAtraso > 0 ? diasAtraso * moraDiaria : 0.0;

            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    "<html><b>¿Confirmar devolución?</b><br><br>" +
                            "<b>Usuario:</b> " + p.getUsuario().getNombreCompleto() + "<br>" +
                            "<b>Ejemplar:</b> " + p.getEjemplar().getTitulo() + "<br>" +
                            "<b>Fecha vencimiento:</b> " + p.getFechaVencimiento() + "<br>" +
                            "<b>Días de atraso:</b> " + (diasAtraso > 0 ? diasAtraso : 0) + "<br>" +
                            (mora > 0 ? "<br><font color='red'><b>Mora a cobrar: S/. " + String.format("%.2f", mora) + "</b></font>" : "<br><font color='green'>Sin mora (devolución a tiempo)</font>") +
                            "</html>",
                    "Devolución", JOptionPane.YES_NO_OPTION);

            if (opcion == JOptionPane.YES_OPTION) {
                p.setFechaDevolucion(new java.sql.Timestamp(System.currentTimeMillis()));
                p.setMoraCalculada(mora);
                p.setEstado("Devuelto");

                if (pdao.actualizar(p)) {
                    new EjemplarDAO().actualizarDisponibilidad(p.getIdEjemplar(), +1);

                    UsuarioDAO udao = new UsuarioDAO();
                    Usuario u = udao.obtenerPorId(p.getIdUsuario());
                    double nuevaMora = u.getMontoMora() + mora;
                    udao.actualizarMora(p.getIdUsuario(), nuevaMora > 0, nuevaMora);

                    String mensaje = mora > 0
                            ? "<html><b>Devolución exitosa</b><br><br>Mora generada: S/. " + String.format("%.2f", mora) + "</html>"
                            : "Devolución exitosa (sin mora)";

                    JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarPrestamos();

                    MainFrame main = (MainFrame) SwingUtilities.getWindowAncestor(this);
                    if (main != null) {
                        main.actualizarEjemplares();
                        main.actualizarUsuarios();
                    }
                }
            }
        }
    }

    private void cargarPrestamos() {
        List<Prestamo> lista = new PrestamoDAO().obtenerActivos();
        modelo.setRowCount(0);
        for (Prestamo p : lista) {
            String estado = p.getFechaDevolucion() == null ? "Activo" : "Devuelto";

            modelo.addRow(new Object[]{
                    p.getIdPrestamo(),
                    p.getUsuario().getNombreCompleto(),
                    p.getEjemplar().getTitulo(),
                    p.getFechaPrestamo(),
                    p.getFechaVencimiento(),
                    estado,
            });
        }
    }

    private void limpiarCampos() {
        txtIdUsuario.setText("");
        txtIdEjemplar.setText("");
    }
}