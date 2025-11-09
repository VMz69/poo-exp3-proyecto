package vista;

import dao.UsuarioDAO;
import model.Usuario;
import model.TipoUsuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel de Usuarios - FASE 1 100%
 * - Muestra: ID, Nombre, Usuario, Correo, Rol, Mora (Sí/No), Monto Mora, Estado
 * - Solo visible para Administrador
 * - Actualización en tiempo real (mora)
 */
public class UsuarioPanel extends JPanel {
    private JTextField txtNombre, txtCorreo, txtUsuario, txtContrasena;
    private JComboBox<TipoUsuario> cmbTipo;
    private JButton btnGuardar, btnLimpiar, btnBuscar, btnCambioPass;
    private JTable tabla;
    private DefaultTableModel modelo;

    public UsuarioPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // === TÍTULO ===
        JLabel titulo = new JLabel("Gestión de Usuarios", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(titulo, BorderLayout.NORTH);

        // === FORMULARIO ===
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNombre = new JTextField(20);
        txtCorreo = new JTextField(20);
        txtUsuario = new JTextField(15);
        txtContrasena = new JPasswordField(15);
        cmbTipo = new JComboBox<>();

        int y = 0;
        addRow(form, gbc, "Nombre Completo:", txtNombre, y++);
        addRow(form, gbc, "Correo:", txtCorreo, y++);
        addRow(form, gbc, "Usuario:", txtUsuario, y++);
        addRow(form, gbc, "Contraseña:", txtContrasena, y++);
        addRow(form, gbc, "Tipo Usuario:", cmbTipo, y++);

        // Botones
        JPanel botones = new JPanel(new FlowLayout());
        btnGuardar = new JButton("Guardar Usuario");
        btnGuardar.setBackground(new Color(40, 167, 69));
        btnGuardar.setForeground(Color.WHITE);
        botones.add(btnGuardar);

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBackground(new Color(108, 117, 125));
        btnLimpiar.setForeground(Color.WHITE);
        botones.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        form.add(botones, gbc);

        add(form, BorderLayout.WEST);

        // === TABLA CON MORA ===
        String[] columnas = {
                "ID", "Nombre", "Usuario", "Correo", "Rol", "Mora", "Monto Mora", "Estado"
        };
        modelo = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.setRowHeight(28);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // === PANEL SUR: BUSCAR ===
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnBuscar = new JButton("Buscar Usuario");
        btnBuscar.setBackground(new Color(0, 123, 255));
        btnBuscar.setForeground(Color.WHITE);
        panelSur.add(btnBuscar);

        btnCambioPass = new JButton("Restablecer Contraseña");
        btnCambioPass.setBackground(new Color(220, 53, 69));
        btnCambioPass.setForeground(Color.WHITE);
        panelSur.add(btnCambioPass);

        add(panelSur, BorderLayout.SOUTH);

        // === ACCIONES ===
        btnGuardar.addActionListener(e -> guardarUsuario());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnBuscar.addActionListener(e -> buscarUsuario());
        btnCambioPass.addActionListener(e -> restablecerPass());

        cargarCombos();
        cargarUsuarios(); // Carga inicial
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int y) {
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void cargarCombos() {
        UsuarioDAO dao = new UsuarioDAO();
        dao.obtenerTiposUsuario().forEach(cmbTipo::addItem);
    }

    // === METODO PÚBLICO: RECARGAR USUARIOS (PARA ACTUALIZAR MORA) ===
    public void cargarUsuarios() {
        List<Usuario> lista = new UsuarioDAO().obtenerTodos();
        modelo.setRowCount(0);
        for (Usuario u : lista) {
            String mora = u.isTieneMora() ? "Sí" : "No";
            String monto = String.format("S/. %.2f", u.getMontoMora());
            String estado = u.isActivo() ? "Activo" : "Inactivo";

            modelo.addRow(new Object[]{
                    u.getIdUsuario(),
                    u.getNombreCompleto(),
                    u.getUsuario(),
                    u.getCorreo(),
                    u.getTipoUsuario().getNombreTipo(),
                    mora,
                    monto,
                    estado
            });
        }
    }

    private void guardarUsuario() {
        try {
            Usuario u = new Usuario();
            u.setNombreCompleto(txtNombre.getText().trim());
            u.setCorreo(txtCorreo.getText().trim());
            u.setUsuario(txtUsuario.getText().trim());
            u.setContrasena(new String(((JPasswordField) txtContrasena).getPassword()));
            u.setTipoUsuario((TipoUsuario) cmbTipo.getSelectedItem());

            if (u.getNombreCompleto().isEmpty() || u.getCorreo().isEmpty() ||
                    u.getUsuario().isEmpty() || u.getContrasena().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (new UsuarioDAO().insertar(u)) {
                JOptionPane.showMessageDialog(this, "Usuario creado con éxito");
                limpiarCampos();
                cargarUsuarios(); // Recarga tabla
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarUsuario() {
        String criterio = JOptionPane.showInputDialog(
                this,
                "Ingrese nombre, usuario o correo:",
                "Búsqueda de Usuario",
                JOptionPane.QUESTION_MESSAGE
        );
        if (criterio == null || criterio.trim().isEmpty()) {
            cargarUsuarios(); // Muestra todos
            return;
        }

        List<Usuario> resultados = new UsuarioDAO().buscar(criterio.trim());
        modelo.setRowCount(0);
        for (Usuario u : resultados) {
            String mora = u.isTieneMora() ? "Sí" : "No";
            String monto = String.format("S/. %.2f", u.getMontoMora());
            String estado = u.isActivo() ? "Activo" : "Inactivo";

            modelo.addRow(new Object[]{
                    u.getIdUsuario(),
                    u.getNombreCompleto(),
                    u.getUsuario(),
                    u.getCorreo(),
                    u.getTipoUsuario().getNombreTipo(),
                    mora,
                    monto,
                    estado
            });
        }
    }

    private void restablecerPass(){
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idUsuario = (int) modelo.getValueAt(fila, 0);
        String nombreUsuarioSelec = (String) modelo.getValueAt(fila,1);
        UsuarioDAO udao = new UsuarioDAO();
        Usuario u = udao.obtenerPorId(idUsuario);

        if (u != null) {
            String nuevaContrasena = JOptionPane.showInputDialog(this, "Ingrese la nueva contraseña para " + nombreUsuarioSelec + ":");

            if (nuevaContrasena != null && !nuevaContrasena.trim().isEmpty()) {
                u.setContrasena(nuevaContrasena);

                if (udao.actualizarContrasena(u)) {
                    JOptionPane.showMessageDialog(this, "Contraseña actualizada correctamente.");
                    //cargarUsuarios(); // RECARGA tabla de usuarios
                } else {
                    JOptionPane.showMessageDialog(this, "Error al actualizar la contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtCorreo.setText("");
        txtUsuario.setText("");
        ((JPasswordField) txtContrasena).setText("");
        cmbTipo.setSelectedIndex(0);
    }
}