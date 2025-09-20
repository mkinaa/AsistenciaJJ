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
            String nombre = nombreField.getText().trim();
            String correo = correoField.getText().trim();
            String contrasena = contrasenaField.getText().trim();
            int rolId = rolBox.getSelectedIndex() + 1;

            // Validación del correo
            if (!correo.matches("^[A-Za-z0-9._%+-]+@gmail\\.com$")) {
                JOptionPane.showMessageDialog(this,
                        "El correo debe ser válido y terminar en @gmail.com",
                        "Error de validación",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validación del nombre
            if (nombre.length() < 2 || nombre.length() > 50 || nombre.matches("\\d+")) {
                JOptionPane.showMessageDialog(this,
                        "El nombre debe tener entre 2 y 50 caracteres y no puede ser solo números",
                        "Error de validación",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validación de contraseña vacía
            if (contrasena.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "La contraseña es obligatoria",
                        "Error de validación",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

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
            "Hora Entrada (HH:MM, opcional):", entradaField,};

        int option = JOptionPane.showConfirmDialog(this, message, "Registrar Asistencia", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int usuarioId = Integer.parseInt(idField.getText());
                Usuario u = usuarioDAO.obtenerPorId(usuarioId);
                if (u == null) {
                    JOptionPane.showMessageDialog(this, "Usuario no existe.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                LocalDate fechaHoy = LocalDate.now();

                // Verificar si ya existe una asistencia para este usuario hoy
                List<Asistencia> asistenciasHoy = asistenciaDAO.listarPorUsuarioYFecha(usuarioId, fechaHoy);
                if (!asistenciasHoy.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Ya existe un registro de entrada para este usuario hoy.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Hora de entrada
                LocalTime horaEntrada = entradaField.getText().isEmpty() ? LocalTime.now() : LocalTime.parse(entradaField.getText());

                // Crear asistencia con solo entrada
                Asistencia asistencia = new Asistencia(0, usuarioId, fechaHoy, horaEntrada, null);
                if (!asistenciaDAO.registrarEntrada(asistencia)) {
                    JOptionPane.showMessageDialog(this, "Error al registrar entrada.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Registrar hora de salida si se ingresó
                if (!salidaField.getText().isEmpty()) {
                    LocalTime horaSalida = LocalTime.parse(salidaField.getText());
                    if (horaSalida.isBefore(horaEntrada)) {
                        JOptionPane.showMessageDialog(this, "La hora de salida no puede ser anterior a la hora de entrada.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    asistenciaDAO.registrarSalida(asistencia.getId(), horaSalida);
                }

                JOptionPane.showMessageDialog(this, "Asistencia registrada correctamente.");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
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
                    String nuevoNombre = nombreField.getText();
                    String nuevoCorreo = correoField.getText();
                    String nuevaContrasena = contrasenaField.getText();
                    int nuevoRolId = rolBox.getSelectedIndex() + 1;

                    // VALIDACIONES
                    if (!nuevoCorreo.matches("^[A-Za-z0-9._%+-]+@gmail\\.com$")) {
                        JOptionPane.showMessageDialog(dialog,
                                "El correo debe ser válido y terminar en @gmail.com",
                                "Error de validación",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (nuevoNombre.length() < 2 || nuevoNombre.length() > 50 || nuevoNombre.matches("\\d+")) {
                        JOptionPane.showMessageDialog(dialog,
                                "El nombre debe tener entre 2 y 50 caracteres y no puede ser solo números",
                                "Error de validación",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Usuario u = new Usuario(id, nuevoNombre, nuevoCorreo,
                            nuevaContrasena.isEmpty() ? null : nuevaContrasena,
                            nuevoRolId);

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

        // Acción Eliminar
        btnEliminar.addActionListener(e -> {
            int fila = table.getSelectedRow();
            if (fila >= 0) {
                int id = (int) model.getValueAt(fila, 0);

                // Validación: no puede eliminarse a sí mismo
                if (id == usuario.getId()) {
                    JOptionPane.showMessageDialog(dialog,
                            "No puedes eliminar el usuario con el que estás logueado.",
                            "Acción no permitida",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

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

            // Validar que el usuario exista
            Usuario usuarioBuscado = usuarioDAO.obtenerPorId(usuarioId);
            if (usuarioBuscado == null) {
                JOptionPane.showMessageDialog(this, "El usuario con ID " + usuarioId + " no existe.", "Error", JOptionPane.ERROR_MESSAGE);
                return; // detener ejecución si no existe
            }

            List<Asistencia> asistencias = asistenciaDAO.listarPorUsuario(usuarioId);
            String[] columnas = {"Fecha", "Hora Entrada", "Hora Salida"};
            DefaultTableModel model = new DefaultTableModel(columnas, 0);

            for (Asistencia a : asistencias) {
                model.addRow(new Object[]{
                    a.getFecha(), a.getHoraEntrada(), a.getHoraSalida()
                });
            }

            JTable table = new JTable(model);
            JOptionPane.showMessageDialog(this, new JScrollPane(table), "Asistencias de " + usuarioBuscado.getNombre(), JOptionPane.PLAIN_MESSAGE);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID inválido.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener asistencias: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
