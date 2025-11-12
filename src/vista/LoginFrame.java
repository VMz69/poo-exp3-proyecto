package vista;

import dao.UsuarioDAO;
import model.Usuario;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    // Campos de entrada
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnLogin;

    // Constructor principal del login
    public LoginFrame() {
        setTitle("Mediateca - Login"); // Título de la ventana
        setSize(400, 300);             // Tamaño fijo de la ventana de login
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Cierra la app al cerrar la ventana
        setLocationRelativeTo(null);   // Centra la ventana en pantalla
        setLayout(new GridBagLayout()); // Usamos GridBagLayout para un diseño flexible

        // Configuración del sistema de coordenadas del GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Márgenes entre componentes

        // ---------- Título ----------
        JLabel lblTitulo = new JLabel("Iniciar Sesión", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24)); // Fuente grande y negrita
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;     // Ocupa 2 columnas
        add(lblTitulo, gbc);

        // ---------- Campo: Usuario ----------
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        add(new JLabel("Usuario:"), gbc); // Etiqueta

        gbc.gridx = 1;
        txtUsuario = new JTextField(15);
        add(txtUsuario, gbc); // Caja de texto

        // ---------- Campo: Contraseña ----------
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Contraseña:"), gbc); // Etiqueta

        gbc.gridx = 1;
        txtContrasena = new JPasswordField(15);
        add(txtContrasena, gbc); // Caja de contraseña

        // ---------- Botón de login ----------
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2; // Centrar el botón
        btnLogin = new JButton("Ingresar");
        btnLogin.setBackground(new Color(0, 123, 255)); // Azul estilo Bootstrap
        btnLogin.setForeground(Color.WHITE);             // Texto blanco
        add(btnLogin, gbc);

        // Acción del botón: llama al método login()
        btnLogin.addActionListener(e -> login());
    }

    /**
     * Método que valida las credenciales e inicia sesión.
     */
    private void login() {
        // Obtiene usuario y contraseña de los campos
        String user = txtUsuario.getText().trim();
        String pass = new String(txtContrasena.getPassword());

        // Verifica que no estén vacíos
        if (user.isEmpty() || pass.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Complete todos los campos",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return; // Sale si faltan datos
        }

        // Crea el DAO para consultar el usuario en la BD
        UsuarioDAO dao = new UsuarioDAO();
        Usuario u = dao.autenticar(user, pass);

        // Si el usuario existe (credenciales válidas)
        if (u != null) {
            dispose(); // Cierra la ventana de login

            // Crea y muestra el MainFrame maximizado
            MainFrame main = new MainFrame(u);
            main.setExtendedState(JFrame.MAXIMIZED_BOTH); // abre maximizado el mainframe
            main.setVisible(true);
        } else {
            // Muestra error si las credenciales no coinciden
            JOptionPane.showMessageDialog(
                    this,
                    "Credenciales incorrectas",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}