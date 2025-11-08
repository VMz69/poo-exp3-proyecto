package vista;

import dao.ConfiguracionDAO;
import model.Configuracion;
import javax.swing.*;
import java.awt.*;

public class ConfiguracionPanel extends JPanel {
    private JTextField txtMaxAlumno, txtMaxProfesor;
    private JTextField txtDiasAlumno, txtDiasProfesor;
    private JTextField txtMoraDiaria;
    private JButton btnGuardar;

    public ConfiguracionPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = new JLabel("Configuración del Sistema", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(titulo, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // === Campos ===
        txtMaxAlumno = new JTextField(10);
        txtMaxProfesor = new JTextField(10);
        txtDiasAlumno = new JTextField(10);
        txtDiasProfesor = new JTextField(10);
        txtMoraDiaria = new JTextField(10);

        int y = 0;
        addRow(form, gbc, "Máx. préstamos Alumno:", txtMaxAlumno, y++);
        addRow(form, gbc, "Máx. préstamos Profesor:", txtMaxProfesor, y++);
        addRow(form, gbc, "Días préstamo Alumno:", txtDiasAlumno, y++);
        addRow(form, gbc, "Días préstamo Profesor:", txtDiasProfesor, y++);
        addRow(form, gbc, "Mora diaria (S/.):", txtMoraDiaria, y++);

        btnGuardar = new JButton("Guardar Configuración");
        btnGuardar.setBackground(new Color(40, 167, 69));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        form.add(btnGuardar, gbc);

        add(form, BorderLayout.CENTER);

        cargarConfiguracion();
        btnGuardar.addActionListener(e -> guardar());
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, String label, JTextField field, int y) {
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void cargarConfiguracion() {
        Configuracion c = new ConfiguracionDAO().obtenerConfiguracion();
        txtMaxAlumno.setText(String.valueOf(c.getMaxPrestamosAlumno()));
        txtMaxProfesor.setText(String.valueOf(c.getMaxPrestamosProfesor()));
        txtDiasAlumno.setText(String.valueOf(c.getDiasPrestamoAlumno()));
        txtDiasProfesor.setText(String.valueOf(c.getDiasPrestamoProfesor()));
        txtMoraDiaria.setText(String.format("%.2f", c.getMoraDiaria()));
    }

    private void guardar() {
        try {
            Configuracion c = new Configuracion();
            c.setMaxPrestamosAlumno(Integer.parseInt(txtMaxAlumno.getText()));
            c.setMaxPrestamosProfesor(Integer.parseInt(txtMaxProfesor.getText()));
            c.setDiasPrestamoAlumno(Integer.parseInt(txtDiasAlumno.getText()));
            c.setDiasPrestamoProfesor(Integer.parseInt(txtDiasProfesor.getText()));
            c.setMoraDiaria(Double.parseDouble(txtMoraDiaria.getText()));
            c.setAnioAplicacion(java.time.Year.now().getValue());

            // Aquí iría un ConfiguracionDAO.actualizar(c), pero como no existe, mostramos mensaje
            JOptionPane.showMessageDialog(this,
                    "Configuración guardada (simulada)\n" +
                            "Alumno: " + c.getMaxPrestamosAlumno() + " préstamos\n" +
                            "Profesor: " + c.getMaxPrestamosProfesor() + " préstamos\n" +
                            "Mora diaria: S/. " + c.getMoraDiaria(),
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese números válidos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}