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
        setTitle("Gestión de Contactos - Swing");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initComponentes() {
        // Panel de formulario
        JPanel panelForm = new JPanel(new GridLayout(5, 2, 10, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelForm.setBackground(new Color(230, 230, 250)); // Fondo lila

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

        // Aquí cambiamos las fuentes y colores de las etiquetas
        Component[] componentes = panelForm.getComponents();
        for (Component comp : componentes) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                label.setFont(new Font("Verdana", Font.BOLD, 14));
                label.setForeground(new Color(50, 50, 100));
            }
        }

        // Botones
        JButton btnGuardar = new JButton("Guardar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnLimpiar = new JButton("Limpiar");

        // Colores de fondo
        btnGuardar.setBackground(new Color(63, 81, 181));    // Azul
        btnEditar.setBackground(new Color(76, 175, 80));     // Verde
        btnEliminar.setBackground(new Color(244, 67, 54));   // Rojo
        btnLimpiar.setBackground(new Color(158, 158, 158));  // Gris

        // Estilo del texto y botones
        JButton[] botones = {btnGuardar, btnEditar, btnEliminar, btnLimpiar};
        for (JButton btn : botones) {
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setFont(new Font("Arial", Font.BOLD, 14));
        }

        // Listeners
        btnGuardar.addActionListener(this::guardarContacto);
        btnEditar.addActionListener(this::editarContacto);
        btnEliminar.addActionListener(this::eliminarContacto);
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setBackground(new Color(230, 230, 250)); // Fondo lila
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

        // Layout principal
        setLayout(new BorderLayout());
        add(panelForm, BorderLayout.NORTH);
        add(new JScrollPane(tablaContactos), BorderLayout.CENTER);
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
