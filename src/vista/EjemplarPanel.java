package vista;

import dao.EjemplarDAO;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.List;

public class EjemplarPanel extends JPanel {

    private JTextField txtTitulo, txtCantidadTotal, txtCantidadDisponible;
    private JComboBox<TipoDocumento> cmbTipo;
    private JComboBox<Categoria> cmbCat;
    private JComboBox<Ubicacion> cmbUbi;

    private JPanel panelCamposDinamicos;

    private JTextField txtAutor, txtEditorial, txtIsbn, txtAnio, txtEdicion, txtIdioma, txtDescripcion;
    private JTextField txtPaginas;
    private JTextField txtDuracion, txtInterprete;
    private JTextField txtUniversidad, txtGrado;
    private JTextField txtPeriodicidad;
    private JTextField txtInstitucion, txtSupervisor;

    // CAMPOS PARA MANUAL
    private JTextField txtVersionManual;

    private JButton btnGuardar, btnBuscar, btnLimpiar, btnEliminar;
    private JTable tabla;
    private DefaultTableModel modelo;

    public EjemplarPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titulo = new JLabel("Gestión de Ejemplares", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        add(titulo, BorderLayout.NORTH);

        JPanel formContainer = new JPanel(new BorderLayout());
        JScrollPane scrollForm = new JScrollPane(formContainer);
        scrollForm.setPreferredSize(new Dimension(490, 500));
        scrollForm.setBorder(BorderFactory.createEmptyBorder());

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        txtTitulo = new JTextField(25);
        cmbTipo = new JComboBox<>();
        cmbCat = new JComboBox<>();
        cmbUbi = new JComboBox<>();
        txtCantidadTotal = new JTextField(10);
        txtCantidadDisponible = new JTextField(10);

        int y = 0;
        addRowObligatorio(form, gbc, "Título:*", txtTitulo, y++);
        addRowObligatorio(form, gbc, "Tipo de Documento:*", cmbTipo, y++);

        panelCamposDinamicos = new JPanel(new GridBagLayout());
        panelCamposDinamicos.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(100, 149, 237), 2),
                        "Información del Documento", 0, 0,
                        new Font("Segoe UI", Font.BOLD, 13), new Color(100, 149, 237)
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        gbc.gridx = 0; gbc.gridy = y++; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 10, 15, 10);
        form.add(panelCamposDinamicos, gbc);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 10, 8, 10);

        addRowObligatorio(form, gbc, "Categoría:*", cmbCat, y++);
        addRowObligatorio(form, gbc, "Ubicación:*", cmbUbi, y++);
        addRowObligatorio(form, gbc, "Cantidad Total:*", txtCantidadTotal, y++);
        // addRowObligatorio(form, gbc, "Cantidad Disponible:*", txtCantidadDisponible, y++);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        btnGuardar = new JButton("Guardar");
        btnGuardar.setBackground(new Color(40, 167, 69));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botones.add(btnGuardar);

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBackground(new Color(108, 117, 125));
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setFocusPainted(false);
        btnLimpiar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLimpiar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botones.add(btnLimpiar);

        gbc.gridx = 0;
        gbc.gridy = y;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        form.add(botones, gbc);

        formContainer.add(form, BorderLayout.NORTH);
        add(scrollForm, BorderLayout.WEST);

        String[] columnas = {
                "ID", "Tipo", "Título", "Autor/Artista", "Categoría",
                "Ubicación", "Total", "Disp.", "Información Específica"
        };
        modelo = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tabla.getTableHeader().setBackground(new Color(52, 73, 94));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setRowHeight(28);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabla.setSelectionBackground(new Color(100, 149, 237));
        tabla.getColumnModel().getColumn(8).setPreferredWidth(250);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel panelSur = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnBuscar = new JButton("Buscar");
        btnBuscar.setBackground(new Color(0, 123, 255));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnBuscar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelSur.add(btnBuscar);

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(220, 53, 69));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelSur.add(btnEliminar);

        add(panelSur, BorderLayout.SOUTH);

        btnGuardar.addActionListener(e -> guardar());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnBuscar.addActionListener(e -> buscarConCriterio());
        btnEliminar.addActionListener(e -> eliminarEjemplar());

        cmbTipo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                actualizarCamposSegunTipo();
            }
        });

        cargarCombos();
        cargarTodos();
    }

    private void addRowObligatorio(JPanel panel, GridBagConstraints gbc, String label, JComponent field, int y) {
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1; gbc.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        if (label.endsWith("*")) lbl.setForeground(new Color(220, 53, 69));
        panel.add(lbl, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private void addRowDinamico(GridBagConstraints gbc, String label, JComponent field, int y, boolean obligatorio) {
        gbc.gridx = 0; gbc.gridy = y; gbc.gridwidth = 1; gbc.weightx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        if (obligatorio) {
            lbl.setForeground(new Color(220, 53, 69));
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        }
        panelCamposDinamicos.add(lbl, gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        panelCamposDinamicos.add(field, gbc);
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
        gbc.anchor = GridBagConstraints.WEST;

        String tipoNombre = tipo.getNombreTipo().toUpperCase().trim();
        int y = 0;

        txtAutor = txtEditorial = txtIsbn = txtAnio = txtEdicion = txtIdioma = txtDescripcion = null;
        txtPaginas = txtDuracion = txtInterprete = null;
        txtUniversidad = txtGrado = null;
        txtPeriodicidad = null;
        txtInstitucion = txtSupervisor = null;

        if (tipoNombre.contains("LIBRO")) {
            txtAutor = new JTextField(25);
            txtEditorial = new JTextField(25);
            txtIsbn = new JTextField(20);
            txtAnio = new JTextField(10);
            txtEdicion = new JTextField(15);
            txtIdioma = new JTextField(15);
            txtPaginas = new JTextField(10);
            txtDescripcion = new JTextField(25);

            addRowDinamico(gbc, "Autor:", txtAutor, y++, false);
            addRowDinamico(gbc, "Editorial:", txtEditorial, y++, false);
            addRowDinamico(gbc, "ISBN:", txtIsbn, y++, false);
            addRowDinamico(gbc, "Año:", txtAnio, y++, false);
            addRowDinamico(gbc, "Edición:", txtEdicion, y++, false);
            addRowDinamico(gbc, "Idioma:", txtIdioma, y++, false);
            addRowDinamico(gbc, "Páginas:", txtPaginas, y++, false);
            addRowDinamico(gbc, "Descripción:", txtDescripcion, y++, false);
        }
        else if (tipoNombre.contains("TESIS")) {
            txtAutor = new JTextField(25);
            txtAnio = new JTextField(10);
            txtIdioma = new JTextField(15);
            txtPaginas = new JTextField(10);
            txtUniversidad = new JTextField(25);
            txtGrado = new JTextField(20);
            txtDescripcion = new JTextField(25);

            addRowDinamico(gbc, "Tesista (Autor):*", txtAutor, y++, true);
            addRowDinamico(gbc, "Universidad:*", txtUniversidad, y++, true);
            addRowDinamico(gbc, "Grado Académico:*", txtGrado, y++, true);
            addRowDinamico(gbc, "Año:", txtAnio, y++, false);
            addRowDinamico(gbc, "Idioma:", txtIdioma, y++, false);
            addRowDinamico(gbc, "Páginas:", txtPaginas, y++, false);
            addRowDinamico(gbc, "Descripción:", txtDescripcion, y++, false);
        }
        else if (tipoNombre.contains("REVISTA")) {
            txtAutor = new JTextField(25);
            txtEditorial = new JTextField(25);
            txtAnio = new JTextField(10);
            txtIdioma = new JTextField(15);
            txtPaginas = new JTextField(10);
//            txtVolumen = new JTextField(15);
//            txtNumero = new JTextField(15);
//            txtISSN = new JTextField(20);
            txtPeriodicidad = new JTextField(20);
            txtDescripcion = new JTextField(25);

            addRowDinamico(gbc, "Autores/Editores:", txtAutor, y++, false);
            addRowDinamico(gbc, "Editorial:", txtEditorial, y++, false);
            addRowDinamico(gbc, "Periodicidad:", txtPeriodicidad, y++, false);
            addRowDinamico(gbc, "Año:", txtAnio, y++, false);
            addRowDinamico(gbc, "Idioma:", txtIdioma, y++, false);
            addRowDinamico(gbc, "Páginas:", txtPaginas, y++, false);
            addRowDinamico(gbc, "Descripción:", txtDescripcion, y++, false);
        }
        else if (tipoNombre.contains("CD")) {
            txtAutor = new JTextField(25);
            txtEditorial = new JTextField(25);
            txtAnio = new JTextField(10);
            txtEdicion = new JTextField(15);
            txtDuracion = new JTextField(10);
            txtInterprete = new JTextField(25);
            txtDescripcion = new JTextField(25);

            addRowDinamico(gbc, "Artista/Banda:", txtInterprete, y++, false);
            addRowDinamico(gbc, "Álbum:", txtAutor, y++, false);
            addRowDinamico(gbc, "Sello Discográfico:", txtEditorial, y++, false);
            addRowDinamico(gbc, "Duración (min):", txtDuracion, y++, false);
            addRowDinamico(gbc, "Año:", txtAnio, y++, false);
            addRowDinamico(gbc, "Edición:", txtEdicion, y++, false);
            addRowDinamico(gbc, "Descripción:", txtDescripcion, y++, false);
        }
        else if (tipoNombre.contains("DVD")) {
            txtAutor = new JTextField(25);
            txtEditorial = new JTextField(25);
            txtAnio = new JTextField(10);
            txtEdicion = new JTextField(15);
            txtIdioma = new JTextField(15);
            txtDuracion = new JTextField(10);
            txtInterprete = new JTextField(25);
            txtDescripcion = new JTextField(25);

            addRowDinamico(gbc, "Director:", txtInterprete, y++, false);
            addRowDinamico(gbc, "Productora:", txtEditorial, y++, false);
            addRowDinamico(gbc, "Duración (min):", txtDuracion, y++, false);
            addRowDinamico(gbc, "Año:", txtAnio, y++, false);
            addRowDinamico(gbc, "Edición:", txtEdicion, y++, false);
            addRowDinamico(gbc, "Idioma:", txtIdioma, y++, false);
            addRowDinamico(gbc, "Descripción:", txtDescripcion, y++, false);
        }
        else if (tipoNombre.contains("INFORME")) {
            txtAutor = new JTextField(25);
            txtAnio = new JTextField(10);
            txtPaginas = new JTextField(10);
            txtInstitucion = new JTextField(25);
            txtSupervisor = new JTextField(25);
            txtDescripcion = new JTextField(25);

            addRowDinamico(gbc, "Autor(es):", txtAutor, y++, false);
            addRowDinamico(gbc, "Institución:", txtInstitucion, y++, false);
            addRowDinamico(gbc, "Supervisor:", txtSupervisor, y++, false);
            addRowDinamico(gbc, "Año:", txtAnio, y++, false);
            addRowDinamico(gbc, "Páginas:", txtPaginas, y++, false);
            addRowDinamico(gbc, "Descripción:", txtDescripcion, y++, false);
        }
        else if (tipoNombre.contains("MANUAL")) {
            txtAutor = new JTextField(25);
            txtEditorial = new JTextField(25);
            txtAnio = new JTextField(10);
            txtPaginas = new JTextField(10);
            txtDescripcion = new JTextField(25);

            txtVersionManual = new JTextField(15);

            addRowDinamico(gbc, "Autor/Responsable:", txtAutor, y++, false);
            addRowDinamico(gbc, "Editorial/Organización:", txtEditorial, y++, false);
            addRowDinamico(gbc, "Versión:", txtVersionManual, y++, false);
            addRowDinamico(gbc, "Año:", txtAnio, y++, false);
            addRowDinamico(gbc, "Páginas:", txtPaginas, y++, false);
            addRowDinamico(gbc, "Descripción:", txtDescripcion, y++, false);
        }
        else {
            JLabel lbl = new JLabel("<html><i>Solo complete los campos comunes.</i></html>");
            lbl.setForeground(Color.GRAY);
            gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
            panelCamposDinamicos.add(lbl, gbc);
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
        String criterio = JOptionPane.showInputDialog(this,
                "Ingrese título, autor o código (vacío para ver todos):",
                "Búsqueda de Ejemplar", JOptionPane.QUESTION_MESSAGE);
        if (criterio == null) return;

        List<Ejemplar> lista = new EjemplarDAO().buscar(criterio.trim());
        actualizarTabla(lista);

        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron ejemplares", "Sin resultados", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void actualizarTabla(List<Ejemplar> lista) {
        modelo.setRowCount(0);
        for (Ejemplar e : lista) {
            String ubicacion = e.getUbicacion().getEdificio() + "-" +
                    e.getUbicacion().getPiso() + "-" +
                    e.getUbicacion().getSeccion() + "-" +
                    e.getUbicacion().getEstante();

            String infoEspecifica = e.getInformacionEspecifica();
            if (infoEspecifica == null || infoEspecifica.isEmpty()) infoEspecifica = "-";

            modelo.addRow(new Object[]{
                    e.getIdEjemplar(),
                    e.getTipoDocumentoString(),
                    e.getTitulo(),
                    //e.getAutor() != null && !e.getAutor().isEmpty() ? e.getAutor() : "-",
                    e.getAutorOArtista(),
                    e.getCategoria().getNombreCategoria(),
                    ubicacion,
                    e.getCantidadTotal(),
                    e.getCantidadTotal(),
                    infoEspecifica
            });
        }
    }

    private void guardar() {
        try {
            if (txtTitulo.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El título es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
                txtTitulo.requestFocus(); return;
            }
            if (txtCantidadTotal.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Las cantidad es obligatoria", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            TipoDocumento tipo = (TipoDocumento) cmbTipo.getSelectedItem();
            if (tipo == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un tipo de documento", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Ejemplar e = EjemplarFactory.crearEjemplar(tipo);
            e.setTitulo(txtTitulo.getText().trim());
            e.setCantidadTotal(Integer.parseInt(txtCantidadTotal.getText().trim()));
            e.setCantidadDisponible(Integer.parseInt(txtCantidadTotal.getText().trim()));
            e.setTipoDocumento(tipo);
            e.setCategoria((Categoria) cmbCat.getSelectedItem());
            e.setUbicacion((Ubicacion) cmbUbi.getSelectedItem());
            e.setFechaIngreso(java.time.LocalDate.now());

            if (txtAutor != null) e.setAutor(txtAutor.getText().trim());
            if (txtEditorial != null) e.setEditorial(txtEditorial.getText().trim());
            if (txtIsbn != null) e.setIsbn(txtIsbn.getText().trim());
            if (txtAnio != null && !txtAnio.getText().trim().isEmpty()) {
                e.setAnioPublicacion(Integer.parseInt(txtAnio.getText().trim()));
            }
            if (txtEdicion != null) e.setNumeroEdicion(txtEdicion.getText().trim());
            if (txtIdioma != null) e.setIdioma(txtIdioma.getText().trim());
            if (txtDescripcion != null) e.setDescripcion(txtDescripcion.getText().trim());

            String tipoNombre = tipo.getNombreTipo().toUpperCase();

            if (tipoNombre.contains("LIBRO")) {
                Libro libro = (Libro) e;
                if (txtPaginas != null && !txtPaginas.getText().trim().isEmpty()) {
                    libro.setNumPaginas(Integer.parseInt(txtPaginas.getText().trim()));
                }
            }
            else if (tipoNombre.contains("TESIS")) {
                Tesis tesis = (Tesis) e;
                if (txtAutor == null || txtAutor.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El tesista (autor) es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
                    txtAutor.requestFocus(); return;
                }
                if (txtUniversidad == null || txtUniversidad.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "La universidad es obligatoria", "Error", JOptionPane.ERROR_MESSAGE);
                    txtUniversidad.requestFocus(); return;
                }
                if (txtGrado == null || txtGrado.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "El grado académico es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
                    txtGrado.requestFocus(); return;
                }
                if (txtPaginas != null && !txtPaginas.getText().trim().isEmpty()) {
                    tesis.setNumPaginas(Integer.parseInt(txtPaginas.getText().trim()));
                }
                tesis.setUniversidad(txtUniversidad.getText().trim());
                tesis.setGradoAcademico(txtGrado.getText().trim());
            }
            else if (tipoNombre.contains("REVISTA")) {
                Revista revista = (Revista) e;
                if (txtPaginas != null && !txtPaginas.getText().trim().isEmpty()) {
                    revista.setNumPaginas(Integer.parseInt(txtPaginas.getText().trim()));
                }
                if (txtPeriodicidad != null) revista.setPeriodicidad(txtPeriodicidad.getText().trim());
            }
            else if (tipoNombre.contains("CD")) {
                CD cd = (CD) e;
                if (txtDuracion != null && !txtDuracion.getText().trim().isEmpty()) {
                    cd.setDuracion(Integer.parseInt(txtDuracion.getText().trim()));
                }
                if (txtInterprete != null) {
                    cd.setArtista(txtInterprete.getText().trim()); // asigan el artista
                }
            }
            else if (tipoNombre.contains("DVD")) {
                DVD dvd = (DVD) e;
                if (txtDuracion != null && !txtDuracion.getText().trim().isEmpty()) {
                    dvd.setDuracion(Integer.parseInt(txtDuracion.getText().trim()));
                }
//                if (txtFormato != null) dvd.setFormato(txtFormato.getText().trim());
                if (txtInterprete != null) dvd.setDirector(txtInterprete.getText().trim());
            }
            else if (tipoNombre.contains("INFORME")) {
                Informe informe = (Informe) e;
                if (txtPaginas != null && !txtPaginas.getText().trim().isEmpty()) {
                    informe.setNumPaginas(Integer.parseInt(txtPaginas.getText().trim()));
                }
                if (txtInstitucion != null) informe.setInstitucion(txtInstitucion.getText().trim());
                if (txtSupervisor != null) informe.setSupervisor(txtSupervisor.getText().trim());
            }
            else if (tipoNombre.contains("MANUAL")) {
                Manual manual = (Manual) e;

                manual.setVersion(txtVersionManual.getText().trim());

                if (txtPaginas != null && !txtPaginas.getText().trim().isEmpty()) {
                    manual.setNumPaginas(Integer.parseInt(txtPaginas.getText().trim()));
                }
            }

            if (new EjemplarDAO().insertar(e)) {
                JOptionPane.showMessageDialog(this,
                        "Ejemplar guardado exitosamente\n\nID: " + e.getIdEjemplar() +
                                "\nTipo: " + e.getTipoDocumentoString() + "\nTítulo: " + e.getTitulo(),
                        "Éxito", JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cargarTodos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar en la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: Verifique que los campos numéricos tengan valores válidos", "Error de formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void eliminarEjemplar() {
        int fila = tabla.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, seleccione un ejemplar de la tabla para eliminar.",
                    "Ningún ejemplar seleccionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idEjemplar = (int) modelo.getValueAt(fila, 0);
        String titulo = (String) modelo.getValueAt(fila, 2);
        String tipo = (String) modelo.getValueAt(fila, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "<html><b>¿Está seguro de eliminar el siguiente ejemplar?</b><br><br>" +
                        "ID: <b>" + idEjemplar + "</b><br>" +
                        "Tipo: <b>" + tipo + "</b><br>" +
                        "Título: <b>" + titulo + "</b><br><br>" +
                        "Esta acción no se puede deshacer.</html>",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        EjemplarDAO dao = new EjemplarDAO();
        if (dao.eliminar(idEjemplar)) {
            JOptionPane.showMessageDialog(this,
                    "Ejemplar eliminado correctamente.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            cargarTodos(); // Recargar tabla
        } else {
            JOptionPane.showMessageDialog(this,
                    "Error al eliminar el ejemplar. Puede estar en uso o ya fue eliminado.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtTitulo.setText("");
        txtCantidadTotal.setText("");
        txtCantidadDisponible.setText("");

        if (cmbTipo.getItemCount() > 0) cmbTipo.setSelectedIndex(0);
        if (cmbCat.getItemCount() > 0) cmbCat.setSelectedIndex(0);
        if (cmbUbi.getItemCount() > 0) cmbUbi.setSelectedIndex(0);

        actualizarCamposSegunTipo();
        txtTitulo.requestFocus();
    }
}