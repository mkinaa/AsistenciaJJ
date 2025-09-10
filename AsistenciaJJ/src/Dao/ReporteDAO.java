package dao;

import model.Reporte;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class ReporteDAO {
    private Connection con;

    public ReporteDAO(Connection con) {
        this.con = con;
    }

    // Insertar en tabla reporte
    public void insertar(Reporte r) throws SQLException {
        String sql = "INSERT INTO reporte (tipo, fechaGeneracion, administrador_id) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, r.getTipo());
            ps.setDate(2, Date.valueOf(r.getFechaGeneracion()));
            ps.setInt(3, r.getAdministradorId());
            ps.executeUpdate();
        }
    }

    // Listar registros de la tabla reporte
    public List<Reporte> listar() throws SQLException {
        List<Reporte> lista = new ArrayList<>();
        String sql = "SELECT * FROM reporte";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Reporte(
                    rs.getInt("id"),
                    rs.getString("tipo"),
                    rs.getDate("fechaGeneracion").toLocalDate(),
                    rs.getInt("administrador_id")
                ));
            }
        }
        return lista;
    }

    // Reporte de atrasos (entradas después de 09:30)
    public List<Object[]> obtenerAtrasos() throws SQLException {
        String sql = """
            SELECT u.id, u.nombre, a.fecha, a.horaEntrada
            FROM asistencia a
            JOIN usuario u ON u.id = a.usuario_id
            WHERE a.horaEntrada > '09:30:00'
        """;
        return ejecutarConsulta(sql);
    }

    // Reporte de salidas anticipadas (antes de 17:30)
    public List<Object[]> obtenerSalidasAnticipadas() throws SQLException {
        String sql = """
            SELECT u.id, u.nombre, a.fecha, a.horaSalida
            FROM asistencia a
            JOIN usuario u ON u.id = a.usuario_id
            WHERE a.horaSalida < '17:30:00'
        """;
        return ejecutarConsulta(sql);
    }

    // Reporte de inasistencias (usuarios que no tienen registro un día laboral)
    public List<Object[]> obtenerInasistencias() throws SQLException {
        String sql = """
            SELECT u.id, u.nombre, d.fecha, NULL as hora
            FROM usuario u
            CROSS JOIN (SELECT DISTINCT fecha FROM asistencia) d
            LEFT JOIN asistencia a ON a.usuario_id = u.id AND a.fecha = d.fecha
            WHERE u.rol_id = 1
              AND a.id IS NULL
        """;
        return ejecutarConsulta(sql);
    }

    // Método genérico para consultas que devuelven datos de reporte
    private List<Object[]> ejecutarConsulta(String sql) throws SQLException {
        List<Object[]> lista = new ArrayList<>();
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Object[] fila = new Object[4];
                fila[0] = rs.getInt(1);      
                fila[1] = rs.getString(2);   
                fila[2] = rs.getDate(3);     
                fila[3] = rs.getString(4);   
                lista.add(fila);
            }
        }
        return lista;
    }
}

