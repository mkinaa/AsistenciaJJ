package app;

import dao.ReporteDAO;
import model.Reporte;
import model.Usuario;
import model.ReportePDF;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReporteGUI extends JFrame {

    private ReporteDAO reporteDAO;
    private Usuario admin; // administrador logueado
    private JTable table;
    private DefaultTableModel model;
    private String tipoReporteActual; // guarda el tipo de reporte seleccionado

    public ReporteGUI(Connection con, Usuario admin) {
        this.reporteDAO = new ReporteDAO(con);
        this.admin = admin;

        setTitle("Generar Reportes");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Panel de botones
        JPanel panelTop = new JPanel(new FlowLayout());
        JButton btnAtrasos = new JButton("Atrasos");
        JButton btnSalidas = new JButton("Salidas Anticipadas");
        JButton btnInasistencias = new JButton("Inasistencias");
        JButton btnExportar = new JButton("Exportar a PDF");

        panelTop.add(btnAtrasos);
        panelTop.add(btnSalidas);
        panelTop.add(btnInasistencias);
        panelTop.add(btnExportar);
        add(panelTop, BorderLayout.NORTH);

        // Tabla
        String[] columnas = {"ID Usuario", "Nombre", "Fecha", "Hora"};
        model = new DefaultTableModel(columnas, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Acciones
        btnAtrasos.addActionListener(e -> cargarReporte("atrasos"));
        btnSalidas.addActionListener(e -> cargarReporte("salidas"));
        btnInasistencias.addActionListener(e -> cargarReporte("inasistencias"));
        btnExportar.addActionListener(e -> exportarPDF());

        setVisible(true);
    }

    private void cargarReporte(String tipo) {
        try {
            tipoReporteActual = tipo; // guardar tipo actual

            List<Object[]> datos;
            switch (tipo) {
                case "atrasos" ->
                    datos = reporteDAO.obtenerAtrasos();
                case "salidas" ->
                    datos = reporteDAO.obtenerSalidasAnticipadas();
                case "inasistencias" ->
                    datos = reporteDAO.obtenerInasistencias();
                default ->
                    throw new IllegalArgumentException("Tipo desconocido");
            }

            // Limpiar tabla
            model.setRowCount(0);
            for (Object[] fila : datos) {
                model.addRow(fila);
            }

            // Registrar en tabla reporte
            Reporte r = new Reporte(0, tipo, LocalDate.now(), admin.getId());
            reporteDAO.insertar(r);

            JOptionPane.showMessageDialog(this, "Reporte de " + tipo + " generado y registrado.");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void exportarPDF() {
        try {
            if (tipoReporteActual == null) {
                JOptionPane.showMessageDialog(this, "Primero genere un reporte para exportar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Fecha y hora más legible
            String fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss"));

            // Nombre dinámico del archivo
            String nombreArchivo = "reporte" + tipoReporteActual.substring(0, 1).toUpperCase()
                    + tipoReporteActual.substring(1) + "_" + fechaHora + ".pdf";

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new java.io.File(nombreArchivo));
            int option = fileChooser.showSaveDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                String ruta = fileChooser.getSelectedFile().getAbsolutePath();
                ReportePDF.exportar("Reporte de " + tipoReporteActual, model, ruta);
                JOptionPane.showMessageDialog(this, "PDF guardado en: " + ruta);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al exportar PDF: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}
