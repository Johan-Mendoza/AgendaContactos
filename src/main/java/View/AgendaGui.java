package View;

import Controllers.ContactosController;
import Models.Contactos;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class AgendaGui extends JFrame {
    private ContactosController controller;
    private JTable tablaContactos;
    private DefaultTableModel modeloTabla;
    private JTextField txtNombre, txtApellido, txtTelefono, txtEmail, txtId;

    public AgendaGui() {
        this.controller = new ContactosController();
        configurarVentana();
        initComponentes();
        cargarContactos();
    }

    private void configurarVentana() {
        setTitle("Gestión de Contactos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(230, 230, 250));
    }

    private void initComponentes() {
        // Panel de formulario
        JPanel panelForm = new JPanel(new GridLayout(5, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelForm.setBackground(new Color(230, 230, 250));

        panelForm.add(new JLabel("ID (para editar):"));
        txtId = new JTextField();
        txtId.setEditable(false);
        panelForm.add(txtId);

        panelForm.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelForm.add(txtNombre);

        panelForm.add(new JLabel("Apellido:"));
        txtApellido = new JTextField();
        panelForm.add(txtApellido);

        panelForm.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        panelForm.add(txtTelefono);

        panelForm.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panelForm.add(txtEmail);

        // Botones
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(this::guardarContacto);

        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(this::editarContacto);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(this::eliminarContacto);

        JButton btnLimpiar = new JButton("Limpiar");
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(new Color(230, 230, 250));
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        // Tabla de contactos
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("ID");
        modeloTabla.addColumn("Nombre");
        modeloTabla.addColumn("Apellido");
        modeloTabla.addColumn("Teléfono");
        modeloTabla.addColumn("Email");

        tablaContactos = new JTable(modeloTabla);
        tablaContactos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaContactos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarContactoDeTabla();
            }
        });

        // Cambiar fondo del viewport del JScrollPane
        JScrollPane scrollPane = new JScrollPane(tablaContactos);
        scrollPane.getViewport().setBackground(new Color(230, 230, 250)); // mismo color lavanda
        scrollPane.setBackground(new Color(230, 230, 250));

        // Layout principal
        setLayout(new BorderLayout());
        add(panelForm, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarContactos() {
        modeloTabla.setRowCount(0); // Limpiar tabla
        List<Contactos> contactos = controller.verContactos();
        for (Contactos c : contactos) {
            modeloTabla.addRow(new Object[]{
                    c.getId(),
                    c.getNombre(),
                    c.getApellido(),
                    c.getTelefono(),
                    c.getEmail()
            });
        }
    }

    private void guardarContacto(ActionEvent e) {
        if (validarCampos()) {
            controller.crearContacto(
                    txtNombre.getText(),
                    txtApellido.getText(),
                    txtTelefono.getText(),
                    txtEmail.getText()
            );
            JOptionPane.showMessageDialog(this, "Contacto guardado!");
            limpiarFormulario();
            cargarContactos();
        }
    }

    private void editarContacto(ActionEvent e) {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un contacto de la tabla para editar.");
            return;
        }

        if (validarCampos()) {
            int id = Integer.parseInt(txtId.getText());
            controller.actualizarContactos(
                    id,
                    txtNombre.getText(),
                    txtApellido.getText(),
                    txtTelefono.getText(),
                    txtEmail.getText()
            );
            JOptionPane.showMessageDialog(this, "Contacto actualizado!");
            limpiarFormulario();
            cargarContactos();
        }
    }

    private void eliminarContacto(ActionEvent e) {
        if (txtId.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Seleccione un contacto de la tabla para eliminar.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Eliminar este contacto?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(txtId.getText());
            controller.eliminar(id);
            JOptionPane.showMessageDialog(this, "Contacto eliminado!");
            limpiarFormulario();
            cargarContactos();
        }
    }

    private void seleccionarContactoDeTabla() {
        int fila = tablaContactos.getSelectedRow();
        if (fila >= 0) {
            txtId.setText(tablaContactos.getValueAt(fila, 0).toString());
            txtNombre.setText(tablaContactos.getValueAt(fila, 1).toString());
            txtApellido.setText(tablaContactos.getValueAt(fila, 2).toString());
            txtTelefono.setText(tablaContactos.getValueAt(fila, 3).toString());
            txtEmail.setText(tablaContactos.getValueAt(fila, 4).toString());
        }
    }

    private boolean validarCampos() {
        if (txtNombre.getText().isEmpty() ||
                txtApellido.getText().isEmpty() ||
                txtTelefono.getText().isEmpty() ||
                txtEmail.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return false;
        }
        return true;
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");
    }

}
