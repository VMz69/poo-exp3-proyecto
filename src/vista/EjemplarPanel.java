package vista;

import dao.EjemplarDAO;
import model.Ejemplar;
import model.TipoDocumento;
import model.Categoria;
import model.Ubicacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;

public class EjemplarPanel extends JPanel {
    private JTextField txtTitulo, txtAutor, txtIsbn, txtAnio, txtEdicion, txtPaginas;
    private JTextField txtEditorial, txtIdioma, txtDescripcion;
    private JTextField txtCantidadTotal, txtCantidadDisponible;

    // Campos específicos para CD/DVD
    private JTextField txtDuracion, txtFormato, txtInterprete;
    private JPanel panelCamposDinamicos;

    private JComboBox<TipoDocumento> cmbTipo;
    private JComboBox<Categoria> cmbCat;
    private JComboBox<Ubicacion> cmbUbi;
    private JButton btnGuardar, btnBuscar, btnLimpiar;
    private JTable tabla;
    private DefaultTableModel modelo;

    public EjemplarPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // === TÍTULO ===
        JLabel titulo = new JLabel("Gestión de Ejemplares", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        add(titulo, BorderLayout.NORTH);

        // === FORMULARIO ===
        JPanel formContainer = new JPanel(new BorderLayout());
        JScrollPane scrollForm = new JScrollPane(formContainer);
        scrollForm.setPreferredSize(new Dimension(450, 600));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Campos comunes
        txtTitulo = new JTextField(20);
        txtAutor = new JTextField(20);
        txtEditorial = new JTextField(20);
        txtIsbn = new JTextField(15);
        txtAnio = new JTextField(5);
        txtEdicion = new JTextField(10);
        txtIdioma = new JTextField(15);
        txtPaginas = new JTextField(5);
        txtDescripcion = new JTextField(20);
        txtCantidadTotal = new JTextField(5);
        txtCantidadDisponible = new JTextField(5);

        cmbTipo = new JComboBox<>();
        cmbCat = new JComboBox<>();
        cmbUbi = new JComboBox<>();

        int y = 0;

        // Campos básicos
        addRow(form, gbc, "Título:*", txtTitulo, y++);
        addRow(form, gbc, "Tipo Documento:*", cmbTipo, y++);

        // Panel dinámico para campos específicos
        panelCamposDinamicos = new JPanel(new GridBagLayout());
        panelCamposDinamicos.setBorder(BorderFactory.createTitledBorder("Campos Específicos"));
        gbc.gridx = 0; gbc.gridy = y++; gbc.gridwidth = 2;
        form.add(panelCamposDinamicos, gbc);
        gbc.gridwidth = 1;

        // Campos comunes continuación
        addRow(form, gbc, "Autor/Artista:", txtAutor, y++);
        addRow(form, gbc, "Editorial/Sello:", txtEditorial, y++);
        addRow(form, gbc, "ISBN/Código:", txtIsbn, y++);
        addRow(form, gbc, "Año Publicación:", txtAnio, y++);
        addRow(form, gbc, "Edición/Versión:", txtEdicion, y++);
        addRow(form, gbc, "Idioma:", txtIdioma, y++);
        addRow(form, gbc, "Categoría:*", cmbCat, y++);
        addRow(form, gbc, "Ubicación:*", cmbUbi, y++);
        addRow(form, gbc, "Descripción:", txtDescripcion, y++);
        addRow(form, gbc, "Cantidad Total:*", txtCantidadTotal, y++);
        addRow(form, gbc, "Cantidad Disponible:*", txtCantidadDisponible, y++);

        // Botones
        JPanel botones = new JPanel(new FlowLayout());
        btnGuardar = new JButton("Guardar Ejemplar");
        btnGuardar.setBackground(new Color(40, 167, 69));
        btnGuardar.setForeground(Color.WHITE);
        botones.add(btnGuardar);

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBackground(new Color(108, 117, 125));
        btnLimpiar.setForeground(Color.WHITE);
        botones.add(btnLimpiar);

        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 2;
        form.add(botones, gbc);

        formContainer.add(form, BorderLayout.NORTH);
        add(scrollForm, BorderLayout.WEST);

        // === TABLA ===
        String[] columnas = {
                "ID", "Título", "Autor/Artista", "Tipo", "Categoría",
                "Ubicación", "Total", "Disponible", "Estado"
        };
        modelo = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.setRowHeight(25);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // === PANEL SUR: BUSCAR ===
        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnBuscar = new JButton("Buscar Ejemplar");
        btnBuscar.setBackground(new Color(0, 123, 255));
        btnBuscar.setForeground(Color.WHITE);
        panelSur.add(btnBuscar);
        add(panelSur, BorderLayout.SOUTH);

        // === ACCIONES ===
        btnGuardar.addActionListener(e -> guardar());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnBuscar.addActionListener(e -> buscarConCriterio());

        // Listener para cambiar campos según tipo
        cmbTipo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                actualizarCamposSegunTipo();
            }
        });

        cargarCombos();
        cargarTodos();
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int y) {
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1;
        JLabel lbl = new JLabel(label);
        if (label.endsWith("*")) {
            lbl.setForeground(Color.RED);
        }
        panel.add(lbl, gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void actualizarCamposSegunTipo() {
        panelCamposDinamicos.removeAll();

        TipoDocumento tipo = (TipoDocumento) cmbTipo.getSelectedItem();
        if (tipo == null) {
            panelCamposDinamicos.revalidate();
            panelCamposDinamicos.repaint();
            return;
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String tipoNombre = tipo.getNombreTipo().toUpperCase();

        if (tipoNombre.contains("CD") || tipoNombre.contains("DVD") ||
                tipoNombre.contains("AUDIO") || tipoNombre.contains("VIDEO")) {

            // Campos para CD/DVD
            txtDuracion = new JTextField(10);
            txtFormato = new JTextField(15);
            txtInterprete = new JTextField(20);

            int y = 0;
            gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1;
            panelCamposDinamicos.add(new JLabel("Duración (min):"), gbc);
            gbc.gridx = 1;
            panelCamposDinamicos.add(txtDuracion, gbc);

            y++;
            gbc.gridx = 0; gbc.gridy = y;
            panelCamposDinamicos.add(new JLabel("Formato:"), gbc);
            gbc.gridx = 1;
            panelCamposDinamicos.add(txtFormato, gbc);

            y++;
            gbc.gridx = 0; gbc.gridy = y;
            panelCamposDinamicos.add(new JLabel("Intérprete/Director:"), gbc);
            gbc.gridx = 1;
            panelCamposDinamicos.add(txtInterprete, gbc);

            // Ocultar campo páginas para CD/DVD
            txtPaginas.setEnabled(false);
            txtPaginas.setText("0");

        } else if (tipoNombre.contains("LIBRO") || tipoNombre.contains("TESIS") ||
                tipoNombre.contains("REVISTA")) {

            // Para libros, mostrar páginas
            txtPaginas.setEnabled(true);
            txtPaginas.setText("");

            JLabel info = new JLabel("Use los campos comunes del formulario");
            info.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            panelCamposDinamicos.add(info, gbc);
        }

        panelCamposDinamicos.revalidate();
        panelCamposDinamicos.repaint();
    }

    private void cargarCombos() {
        EjemplarDAO dao = new EjemplarDAO();
        dao.obtenerTiposDocumento().forEach(cmbTipo::addItem);
        dao.obtenerCategorias().forEach(cmbCat::addItem);
        dao.obtenerUbicaciones().forEach(cmbUbi::addItem);
    }

    public void cargarTodos() {
        List<Ejemplar> lista = new EjemplarDAO().buscar("");
        actualizarTabla(lista);
    }

    private void buscarConCriterio() {
        String criterio = JOptionPane.showInputDialog(
                this,
                "Ingrese título, autor o ISBN (deje vacío para ver todos):",
                "Búsqueda de Ejemplar",
                JOptionPane.QUESTION_MESSAGE
        );
        if (criterio == null) return;

        List<Ejemplar> lista = new EjemplarDAO().buscar(criterio.trim());
        actualizarTabla(lista);
    }

    private void actualizarTabla(List<Ejemplar> lista) {
        modelo.setRowCount(0);
        for (Ejemplar e : lista) {
            String ubicacion = e.getUbicacion().getEdificio() + "-" +
                    e.getUbicacion().getPiso() + "-" +
                    e.getUbicacion().getSeccion() + "-" +
                    e.getUbicacion().getEstante();

            String estado = e.getCantidadDisponible() > 0 ? "Disponible" : "Prestado";

            modelo.addRow(new Object[]{
                    e.getIdEjemplar(),
                    e.getTitulo(),
                    e.getAutor(),
                    e.getTipoDocumento().getNombreTipo(),
                    e.getCategoria().getNombreCategoria(),
                    ubicacion,
                    e.getCantidadTotal(),
                    e.getCantidadDisponible(),
                    estado
            });
        }
    }

    private void guardar() {
        try {
            // Validaciones básicas
            if (txtTitulo.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El título es obligatorio",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (txtCantidadTotal.getText().trim().isEmpty() ||
                    txtCantidadDisponible.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Las cantidades son obligatorias",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Ejemplar e = new Ejemplar();
            e.setTitulo(txtTitulo.getText().trim());
            e.setAutor(txtAutor.getText().trim());
            e.setEditorial(txtEditorial.getText().trim());
            e.setIsbn(txtIsbn.getText().trim());
            e.setAnioPublicacion(txtAnio.getText().isEmpty() ? 0 : Integer.parseInt(txtAnio.getText()));
            e.setNumeroEdicion(txtEdicion.getText().trim());
            e.setIdioma(txtIdioma.getText().trim());
            e.setNumPaginas(txtPaginas.getText().isEmpty() ? 0 : Integer.parseInt(txtPaginas.getText()));
            e.setCantidadTotal(Integer.parseInt(txtCantidadTotal.getText().trim()));
            e.setCantidadDisponible(Integer.parseInt(txtCantidadDisponible.getText().trim()));
            e.setTipoDocumento((TipoDocumento) cmbTipo.getSelectedItem());
            e.setCategoria((Categoria) cmbCat.getSelectedItem());
            e.setUbicacion((Ubicacion) cmbUbi.getSelectedItem());
            e.setFechaIngreso(java.time.LocalDate.now());

            // Construir descripción con campos específicos
            StringBuilder desc = new StringBuilder();
            if (txtDescripcion.getText() != null && !txtDescripcion.getText().trim().isEmpty()) {
                desc.append(txtDescripcion.getText().trim());
            }

            // Agregar información de CD/DVD a la descripción
            TipoDocumento tipo = (TipoDocumento) cmbTipo.getSelectedItem();
            if (tipo != null) {
                String tipoNombre = tipo.getNombreTipo().toUpperCase();
                if (tipoNombre.contains("CD") || tipoNombre.contains("DVD") ||
                        tipoNombre.contains("AUDIO") || tipoNombre.contains("VIDEO")) {

                    if (txtDuracion != null && !txtDuracion.getText().trim().isEmpty()) {
                        if (desc.length() > 0) desc.append(" | ");
                        desc.append("Duración: ").append(txtDuracion.getText()).append(" min");
                    }
                    if (txtFormato != null && !txtFormato.getText().trim().isEmpty()) {
                        if (desc.length() > 0) desc.append(" | ");
                        desc.append("Formato: ").append(txtFormato.getText());
                    }
                    if (txtInterprete != null && !txtInterprete.getText().trim().isEmpty()) {
                        if (desc.length() > 0) desc.append(" | ");
                        desc.append("Intérprete/Director: ").append(txtInterprete.getText());
                    }
                }
            }

            e.setDescripcion(desc.toString());

            if (new EjemplarDAO().insertar(e)) {
                JOptionPane.showMessageDialog(this,
                        "Ejemplar guardado con éxito\nID: " + e.getIdEjemplar(),
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarTodos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar el ejemplar",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error en los números ingresados. Verifique año, páginas y cantidades",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void limpiarCampos() {
        txtTitulo.setText("");
        txtAutor.setText("");
        txtEditorial.setText("");
        txtIsbn.setText("");
        txtAnio.setText("");
        txtEdicion.setText("");
        txtIdioma.setText("");
        txtPaginas.setText("");
        txtDescripcion.setText("");
        txtCantidadTotal.setText("");
        txtCantidadDisponible.setText("");

        if (cmbTipo.getItemCount() > 0) cmbTipo.setSelectedIndex(0);
        if (cmbCat.getItemCount() > 0) cmbCat.setSelectedIndex(0);
        if (cmbUbi.getItemCount() > 0) cmbUbi.setSelectedIndex(0);

        // Limpiar campos dinámicos
        panelCamposDinamicos.removeAll();
        panelCamposDinamicos.revalidate();
        panelCamposDinamicos.repaint();

        txtPaginas.setEnabled(true);
    }
}