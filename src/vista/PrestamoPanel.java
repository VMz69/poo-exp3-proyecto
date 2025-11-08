package vista;

import dao.PrestamoDAO;
import dao.EjemplarDAO;
import dao.UsuarioDAO;
import model.Prestamo;
import model.Usuario;
import model.Ejemplar;

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

        btnBuscarUsuario = new JButton("Buscar Usuario");
        btnBuscarEjemplar = new JButton("Buscar Ejemplar");
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
        String[] columnas = {"ID", "Usuario", "Ejemplar", "Préstamo", "Vencimiento", "Estado", "Mora"};
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
        Usuario u = new UsuarioDAO().obtenerPorId(Integer.parseInt(id));
        if (u != null) {
            JOptionPane.showMessageDialog(this,
                    "<html><b>Usuario encontrado:</b><br>" +
                            u.getNombreCompleto() + " (" + u.getTipoUsuario().getNombreTipo() + ")<br>" +
                            (u.isTieneMora() ? "<font color='red'>Mora: S/. " + u.getMontoMora() + "</font>" : "Sin mora") +
                            "</html>", "Usuario", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Usuario no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarEjemplar() {
        String id = txtIdEjemplar.getText().trim();
        if (id.isEmpty()) return;
        Ejemplar e = new EjemplarDAO().obtenerPorId(Integer.parseInt(id));
        if (e != null && e.getCantidadDisponible() > 0) {
            JOptionPane.showMessageDialog(this,
                    "<html><b>Ejemplar disponible:</b><br>" +
                            e.getTitulo() + " - " + e.getAutor() + "<br>" +
                            "Disponible: " + e.getCantidadDisponible() +
                            "</html>", "Ejemplar", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Ejemplar no encontrado o no disponible", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hacerPrestamo() {
        try {
            int idUsuario = Integer.parseInt(txtIdUsuario.getText().trim());
            int idEjemplar = Integer.parseInt(txtIdEjemplar.getText().trim());

            UsuarioDAO udao = new UsuarioDAO();
            EjemplarDAO edao = new EjemplarDAO();
            PrestamoDAO pdao = new PrestamoDAO();

            // VALIDACIÓN MORA
            if (udao.tieneMora(idUsuario)) {
                JOptionPane.showMessageDialog(this, "Usuario con mora. No puede prestar.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // VALIDACIÓN DISPONIBILIDAD
            Ejemplar e = edao.obtenerPorId(idEjemplar);
            if (e == null || e.getCantidadDisponible() <= 0) {
                JOptionPane.showMessageDialog(this, "Ejemplar no disponible.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // CREAR PRÉSTAMO
            Prestamo p = new Prestamo();
            p.setIdUsuario(idUsuario);
            p.setIdEjemplar(idEjemplar);

            Usuario u = udao.obtenerPorId(idUsuario);
            int dias = u.getTipoUsuario().getNombreTipo().equals("Profesor") ? 14 : 7;
            LocalDate vencimiento = LocalDate.now().plusDays(dias);
            p.setFechaVencimiento(java.sql.Date.valueOf(vencimiento)); // CORREGIDO

            if (pdao.insertar(p)) {
                edao.actualizarDisponibilidad(idEjemplar, -1);
                JOptionPane.showMessageDialog(this, "Préstamo realizado con éxito");
                limpiarCampos();
                cargarPrestamos();

                MainFrame main = (MainFrame) SwingUtilities.getWindowAncestor(this);
                main.actualizarEjemplares();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese IDs válidos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void devolverPrestamo() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un préstamo", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idPrestamo = (int) modelo.getValueAt(fila, 0);
        PrestamoDAO pdao = new PrestamoDAO();
        Prestamo p = pdao.obtenerPorId(idPrestamo);

        if (p != null && p.getFechaDevolucion() == null) {
            double moraDiaria = 1.50;
            long diasAtraso = java.time.temporal.ChronoUnit.DAYS.between(
                    p.getFechaVencimiento().toLocalDate(), LocalDate.now());
            double mora = diasAtraso > 0 ? diasAtraso * moraDiaria : 0.0;

            int opcion = JOptionPane.showConfirmDialog(
                    this,
                    "<html><b>¿Devolver ejemplar?</b><br>" +
                            "Título: " + p.getEjemplar().getTitulo() + "<br>" +
                            (mora > 0 ? "<font color='red'>Mora: S/. " + String.format("%.2f", mora) + "</font>" : "Sin mora") +
                            "</html>", "Devolución", JOptionPane.YES_NO_OPTION);

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

                    JOptionPane.showMessageDialog(this, "Devolución exitosa" + (mora > 0 ? " (Mora: S/. " + mora + ")" : ""));
                    cargarPrestamos();

                    MainFrame main = (MainFrame) SwingUtilities.getWindowAncestor(this);
                    main.actualizarEjemplares();
                    main.actualizarUsuarios();
                }
            }
        }
    }

    private void cargarPrestamos() {
        List<Prestamo> lista = new PrestamoDAO().obtenerActivos();
        modelo.setRowCount(0);
        for (Prestamo p : lista) {
            double mora = p.getMoraCalculada();
            String estado = p.getFechaDevolucion() == null ? "Activo" : "Devuelto";

            modelo.addRow(new Object[]{
                    p.getIdPrestamo(),
                    p.getUsuario().getNombreCompleto(),
                    p.getEjemplar().getTitulo(),
                    p.getFechaPrestamo(),
                    p.getFechaVencimiento(),
                    estado,
                    mora > 0 ? "S/. " + String.format("%.2f", mora) : "-"
            });
        }
    }

    private void limpiarCampos() {
        txtIdUsuario.setText("");
        txtIdEjemplar.setText("");
    }
}