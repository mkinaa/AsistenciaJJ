package app;

import dao.UsuarioDAO;
import dao.AsistenciaDAO;
import model.Usuario;
import model.Asistencia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class MainGUI extends JFrame {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private AsistenciaDAO asistenciaDAO = new AsistenciaDAO();

    private Usuario usuario; // agregamos usuario

    // Constructor ahora recibe el usuario
    public MainGUI(Usuario usuario) {
        this.usuario = usuario;

        setTitle("Control de Asistencia");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout()); // cambiamos a BorderLayout para poder poner el botón arriba

        // Panel superior con nombre del usuario y botón de cerrar sesión
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // JLabel con nombre y rol
        String rolStr = usuario.getRolId() == 2 ? "Administrador" : "Empleado";
        JLabel lblUsuario = new JLabel(usuario.getNombre() + " - " + rolStr);
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 14));
        panelTop.add(lblUsuario);

        JButton btnCerrarSesion = new JButton("Cerrar sesión");
        panelTop.add(btnCerrarSesion);
        add(panelTop, BorderLayout.NORTH);

        btnCerrarSesion.addActionListener(e -> {
            this.dispose(); // cierra esta ventana
            SwingUtilities.invokeLater(LoginGUI::new); // vuelve al login
        });

        // Panel central con los botones actuales
        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 10, 10));

        JButton btnCrearUsuario = new JButton("Crear Usuario");
        JButton btnRegistrarAsistencia = new JButton("Registrar Asistencia");
        JButton btnListarUsuarios = new JButton("Listar Usuarios");
        JButton btnListarAsistencias = new JButton("Listar Asistencias");
        JButton btnReportes = new JButton("Generar Reportes");
        

        panelBotones.add(btnCrearUsuario);
        panelBotones.add(btnRegistrarAsistencia);
        panelBotones.add(btnListarUsuarios);
        panelBotones.add(btnListarAsistencias);
        if (usuario.getRolId() == 2) { // Solo administradores
        panelBotones.add(btnReportes);
        }           

        add(panelBotones, BorderLayout.CENTER);

        // Acciones
        btnCrearUsuario.addActionListener(e -> mostrarCrearUsuario());
        btnRegistrarAsistencia.addActionListener(e -> mostrarRegistrarAsistencia());
        btnListarUsuarios.addActionListener(e -> mostrarListaUsuarios());
        btnListarAsistencias.addActionListener(e -> mostrarListaAsistencias());
        btnReportes.addActionListener(e -> {
            try {
                new ReporteGUI(bd.Conexion.getConnection(), usuario);
            } catch (SQLException ex) {
                System.getLogger(MainGUI.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });

        setVisible(true);
    }

    private void mostrarCrearUsuario() {
        JTextField nombreField = new JTextField();
        JTextField correoField = new JTextField();
        JTextField contrasenaField = new JTextField();
        String[] roles = {"1 - Empleado", "2 - Administrador"};
        JComboBox<String> rolBox = new JComboBox<>(roles);

        Object[] message = {
                "Nombre:", nombreField,
                "Correo:", correoField,
                "Contraseña:", contrasenaField,
                "Rol:", rolBox
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Crear Usuario", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String nombre = nombreField.getText();
            String correo = correoField.getText();
            String contrasena = contrasenaField.getText();
            int rolId = rolBox.getSelectedIndex() + 1;

            Usuario nuevo = new Usuario(0, nombre, correo, contrasena, rolId);
            if (usuarioDAO.insertar(nuevo)) {
                JOptionPane.showMessageDialog(this, "Usuario creado correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Error al crear usuario.");
            }
        }
    }

    private void mostrarRegistrarAsistencia() {
        JTextField idField = new JTextField();
        JTextField entradaField = new JTextField();
        JTextField salidaField = new JTextField();

        Object[] message = {
                "ID Usuario:", idField,
                "Hora Entrada (HH:MM, opcional):", entradaField,
                "Hora Salida (HH:MM, opcional):", salidaField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Registrar Asistencia", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int usuarioId = Integer.parseInt(idField.getText());
                Usuario u = usuarioDAO.obtenerPorId(usuarioId);
                if (u == null) {
                    JOptionPane.showMessageDialog(this, "Usuario no existe.");
                    return;
                }

                LocalTime horaEntrada = entradaField.getText().isEmpty() ? LocalTime.now() : LocalTime.parse(entradaField.getText());
                LocalTime horaSalida = salidaField.getText().isEmpty() ? LocalTime.now() : LocalTime.parse(salidaField.getText());

                Asistencia asistencia = new Asistencia(0, usuarioId, LocalDate.now(), horaEntrada, horaSalida);
                if (asistenciaDAO.registrarEntrada(asistencia)) {
                    if (horaSalida != null) {
                        asistenciaDAO.registrarSalida(asistencia.getId(), horaSalida);
                    }
                    JOptionPane.showMessageDialog(this, "Asistencia registrada correctamente.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    }

    private void mostrarListaUsuarios() {
    List<Usuario> usuarios = usuarioDAO.listar();
    String[] columnas = {"ID", "Nombre", "Correo", "Rol"};
    DefaultTableModel model = new DefaultTableModel(columnas, 0);

    for (Usuario u : usuarios) {
        model.addRow(new Object[]{u.getId(), u.getNombre(), u.getCorreo(), u.getRolId()});
    }

    JTable table = new JTable(model);
    JScrollPane scrollPane = new JScrollPane(table);

    JButton btnEditar = new JButton("Editar");
    JButton btnEliminar = new JButton("Eliminar");

    JPanel panel = new JPanel(new BorderLayout());
    panel.add(scrollPane, BorderLayout.CENTER);

    JPanel panelBotones = new JPanel();
    panelBotones.add(btnEditar);
    panelBotones.add(btnEliminar);
    panel.add(panelBotones, BorderLayout.SOUTH);

    JDialog dialog = new JDialog(this, "Usuarios", true);
    dialog.add(panel);
    dialog.setSize(500, 300);
    dialog.setLocationRelativeTo(this);

    // Acción Editar
    btnEditar.addActionListener(e -> {
        int fila = table.getSelectedRow();
        if (fila >= 0) {
            int id = (int) model.getValueAt(fila, 0);
            String nombre = (String) model.getValueAt(fila, 1);
            String correo = (String) model.getValueAt(fila, 2);
            int rolId = (int) model.getValueAt(fila, 3);

            JTextField nombreField = new JTextField(nombre);
            JTextField correoField = new JTextField(correo);
            JTextField contrasenaField = new JTextField();
            String[] roles = {"1 - Empleado", "2 - Administrador"};
            JComboBox<String> rolBox = new JComboBox<>(roles);
            rolBox.setSelectedIndex(rolId - 1);

            Object[] message = {
                "Nombre:", nombreField,
                "Correo:", correoField,
                "Contraseña (OBLIGATORIO):", contrasenaField,
                "Rol:", rolBox
            };

            int option = JOptionPane.showConfirmDialog(dialog, message, "Editar Usuario", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                Usuario u = new Usuario(id, nombreField.getText(), correoField.getText(),
                        contrasenaField.getText().isEmpty() ? null : contrasenaField.getText(),
                        rolBox.getSelectedIndex() + 1);

                if (usuarioDAO.actualizar(u)) {
                    JOptionPane.showMessageDialog(dialog, "Usuario actualizado.");
                    dialog.dispose();
                    mostrarListaUsuarios(); // refrescar
                } else {
                    JOptionPane.showMessageDialog(dialog, "Error al actualizar.");
                }
            }
        }
    });

    btnEliminar.addActionListener(e -> {
        int fila = table.getSelectedRow();
        if (fila >= 0) {
            int id = (int) model.getValueAt(fila, 0);
            int confirm = JOptionPane.showConfirmDialog(dialog, "¿Seguro de eliminar este usuario?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (usuarioDAO.eliminar(id)) {
                    JOptionPane.showMessageDialog(dialog, "Usuario eliminado.");
                    dialog.dispose();
                    mostrarListaUsuarios(); // refrescar
                } else {
                    JOptionPane.showMessageDialog(dialog, "Error al eliminar.");
                }
            }
        }
    });

    dialog.setVisible(true);
}


    private void mostrarListaAsistencias() {
        String input = JOptionPane.showInputDialog(this, "Ingrese ID del usuario:");
        try {
            int usuarioId = Integer.parseInt(input);
            List<Asistencia> asistencias = asistenciaDAO.listarPorUsuario(usuarioId);
            String[] columnas = {"ID", "Fecha", "Hora Entrada", "Hora Salida"};
            DefaultTableModel model = new DefaultTableModel(columnas, 0);

            for (Asistencia a : asistencias) {
                model.addRow(new Object[]{
                        a.getId(), a.getFecha(), a.getHoraEntrada(), a.getHoraSalida()
                });
            }

            JTable table = new JTable(model);
            JOptionPane.showMessageDialog(this, new JScrollPane(table), "Asistencias", JOptionPane.PLAIN_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido.");
        }
    }

  
}

