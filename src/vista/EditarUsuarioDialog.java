package vista;

import dao.UsuarioDAO;
import model.Usuario;
import model.TipoUsuario;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


public class EditarUsuarioDialog extends JDialog {

    private JTextField txtNombre;
    private JTextField txtUsuario;
    private JTextField txtCorreo;
    private JComboBox<TipoUsuario> comboRol;

    private Usuario usuario;

    public EditarUsuarioDialog(Frame parent, Usuario usuario) {
        super(parent, "Editar Usuario", true);
        this.usuario = usuario;

        setLayout(new BorderLayout(10, 10));
        setSize(400, 300);
        setLocationRelativeTo(parent);

        // Panel principal
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Nombre completo:"));
        txtNombre = new JTextField(usuario.getNombreCompleto());
        panel.add(txtNombre);

        panel.add(new JLabel("Usuario:"));
        txtUsuario = new JTextField(usuario.getUsuario());
        panel.add(txtUsuario);

        panel.add(new JLabel("Correo:"));
        txtCorreo = new JTextField(usuario.getCorreo());
        panel.add(txtCorreo);

        panel.add(new JLabel("Rol de usuario:"));
        comboRol = new JComboBox<>();
        cargarRoles();
        comboRol.setSelectedItem(usuario.getTipoUsuario());
        panel.add(comboRol);

        add(panel, BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        btnGuardar.addActionListener((ActionEvent e) -> {
            guardarCambios();
        });

        btnCancelar.addActionListener((ActionEvent e) -> {
            dispose();
        });

        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarRoles() {
        UsuarioDAO dao = new UsuarioDAO();
        dao.obtenerTiposUsuario().forEach(comboRol::addItem);
    }

    private void guardarCambios() {
        usuario.setNombreCompleto(txtNombre.getText());
        usuario.setUsuario(txtUsuario.getText());
        usuario.setCorreo(txtCorreo.getText());
        usuario.setTipoUsuario((TipoUsuario) comboRol.getSelectedItem());

        UsuarioDAO udao = new UsuarioDAO();
        udao.actualizar(usuario);
        dispose();
    }

}
