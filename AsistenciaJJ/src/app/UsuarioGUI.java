package app;

import dao.AsistenciaDAO;
import model.Usuario;
import model.Asistencia;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class UsuarioGUI extends JFrame {

    private AsistenciaDAO asistenciaDAO = new AsistenciaDAO();
    private Usuario usuario;
    private DefaultTableModel tableModel;

    public UsuarioGUI(Usuario usuario) {
        this.usuario = usuario;
        mostrarPanelUsuario();
    }

    private void mostrarPanelUsuario() {
    setTitle("Registro de Asistencia - " + usuario.getNombre());
    setSize(600, 400);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setLayout(new BorderLayout());

    // Panel superior con fecha y hora
    JPanel panelTop = new JPanel(new GridLayout(1, 2, 10, 10));
    JLabel lblFecha = new JLabel("Fecha: " + LocalDate.now());
    JLabel lblHora = new JLabel("Hora: " + LocalTime.now().withSecond(0).withNano(0));
    panelTop.add(lblFecha);
    panelTop.add(lblHora);
    add(panelTop, BorderLayout.NORTH);

    // Panel central con tabla de asistencias
    String[] columnas = {"ID", "Fecha", "Hora Entrada", "Hora Salida"};
    tableModel = new DefaultTableModel(columnas, 0);
    JTable table = new JTable(tableModel);
    add(new JScrollPane(table), BorderLayout.CENTER);

    // Panel inferior con botones
    JPanel panelBotones = new JPanel(new GridLayout(1, 3, 10, 10)); // ahora 3 botones
    JButton btnEntrada = new JButton("Registrar Entrada");
    JButton btnSalida = new JButton("Registrar Salida");
    JButton btnCerrarSesion = new JButton("Cerrar sesión"); // nuevo botón
    panelBotones.add(btnEntrada);
    panelBotones.add(btnSalida);
    panelBotones.add(btnCerrarSesion);
    add(panelBotones, BorderLayout.SOUTH);

    // Cargar historial del día
    actualizarTabla();

    // Acciones botones
    btnEntrada.addActionListener(e -> registrarEntrada());
    btnSalida.addActionListener(e -> registrarSalida());
    btnCerrarSesion.addActionListener(e -> {
        this.dispose(); // cierra ventana actual
        javax.swing.SwingUtilities.invokeLater(LoginGUI::new); // vuelve al login
    });

    setVisible(true);
}


    private void registrarEntrada() {
        LocalDate hoy = LocalDate.now();
        List<Asistencia> asistencias = asistenciaDAO.listarPorUsuario(usuario.getId());
        boolean entradaHoy = asistencias.stream()
                .anyMatch(a -> a.getFecha().equals(hoy) && a.getHoraEntrada() != null);

        if (entradaHoy) {
            JOptionPane.showMessageDialog(this, "La entrada ya ha sido registrada hoy.");
            return;
        }

        Asistencia asistencia = new Asistencia(0, usuario.getId(), hoy, LocalTime.now().withSecond(0).withNano(0), null);
        if (asistenciaDAO.registrarEntrada(asistencia)) {
            JOptionPane.showMessageDialog(this, "Entrada registrada correctamente.");
            actualizarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar entrada.");
        }
    }

    private void registrarSalida() {
        LocalDate hoy = LocalDate.now();
        List<Asistencia> asistencias = asistenciaDAO.listarPorUsuario(usuario.getId());
        Asistencia asistenciaHoy = asistencias.stream()
                .filter(a -> a.getFecha().equals(hoy) && a.getHoraSalida() == null && a.getHoraEntrada() != null)
                .findFirst()
                .orElse(null);

        if (asistenciaHoy == null) {
            JOptionPane.showMessageDialog(this, "No hay entrada registrada hoy o ya se registró la salida.");
            return;
        }

        if (asistenciaDAO.registrarSalida(asistenciaHoy.getId(), LocalTime.now().withSecond(0).withNano(0))) {
            JOptionPane.showMessageDialog(this, "Salida registrada correctamente.");
            actualizarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Error al registrar salida.");
        }
    }

    private void actualizarTabla() {
        tableModel.setRowCount(0);
        List<Asistencia> asistencias = asistenciaDAO.listarPorUsuario(usuario.getId());
        for (Asistencia a : asistencias) {
            tableModel.addRow(new Object[]{
                    a.getId(), a.getFecha(), a.getHoraEntrada(), a.getHoraSalida()
            });
        }
    }
}


