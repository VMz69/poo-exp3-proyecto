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
            // Validar valores
            int maxAlumno = Integer.parseInt(txtMaxAlumno.getText().trim());
            int maxProfesor = Integer.parseInt(txtMaxProfesor.getText().trim());
            int diasAlumno = Integer.parseInt(txtDiasAlumno.getText().trim());
            int diasProfesor = Integer.parseInt(txtDiasProfesor.getText().trim());
            double moraDiaria = Double.parseDouble(txtMoraDiaria.getText().trim());

            // Validaciones de rango
            if (maxAlumno < 1 || maxAlumno > 10) {
                JOptionPane.showMessageDialog(this,
                        "Máx. préstamos alumno debe estar entre 1 y 10",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (maxProfesor < 1 || maxProfesor > 20) {
                JOptionPane.showMessageDialog(this,
                        "Máx. préstamos profesor debe estar entre 1 y 20",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (diasAlumno < 1 || diasAlumno > 30) {
                JOptionPane.showMessageDialog(this,
                        "Días préstamo alumno debe estar entre 1 y 30",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (diasProfesor < 1 || diasProfesor > 90) {
                JOptionPane.showMessageDialog(this,
                        "Días préstamo profesor debe estar entre 1 y 90",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (moraDiaria < 0 || moraDiaria > 10) {
                JOptionPane.showMessageDialog(this,
                        "Mora diaria debe estar entre 0 y 10",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Crear objeto configuración
            Configuracion c = new Configuracion();
            c.setMaxPrestamosAlumno(maxAlumno);
            c.setMaxPrestamosProfesor(maxProfesor);
            c.setDiasPrestamoAlumno(diasAlumno);
            c.setDiasPrestamoProfesor(diasProfesor);
            c.setMoraDiaria(moraDiaria);
            c.setAnioAplicacion(java.time.Year.now().getValue());

            // Guardar en base de datos
            ConfiguracionDAO dao = new ConfiguracionDAO();
            boolean exito = dao.guardarConfiguracion(c);

            if (exito) {
                JOptionPane.showMessageDialog(this,
                        "<html><b>Configuración guardada exitosamente</b><br><br>" +
                                "<b>Alumnos:</b><br>" +
                                "• Máx. préstamos: " + c.getMaxPrestamosAlumno() + "<br>" +
                                "• Días de préstamo: " + c.getDiasPrestamoAlumno() + "<br><br>" +
                                "<b>Profesores:</b><br>" +
                                "• Máx. préstamos: " + c.getMaxPrestamosProfesor() + "<br>" +
                                "• Días de préstamo: " + c.getDiasPrestamoProfesor() + "<br><br>" +
                                "<b>Mora diaria:</b> S/. " + String.format("%.2f", c.getMoraDiaria()) + "<br><br>" +
                                "<i>Los cambios se aplicarán a todos los préstamos nuevos.</i>" +
                                "</html>",
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Error al guardar la configuración en la base de datos.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Todos los campos deben contener números válidos.\n" +
                            "Use punto (.) como separador decimal para la mora.",
                    "Error de formato", JOptionPane.ERROR_MESSAGE);
        }
    }
}