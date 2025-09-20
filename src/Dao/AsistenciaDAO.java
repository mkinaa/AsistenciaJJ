package dao;

import bd.Conexion;
import model.Asistencia;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AsistenciaDAO {

    // INSERT
    public boolean registrarEntrada(Asistencia a) {
        String sql = "INSERT INTO Asistencia (usuario_id, fecha, horaEntrada, horaSalida) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, a.getUsuarioId());
            ps.setDate(2, Date.valueOf(a.getFecha()));
            ps.setTime(3, a.getHoraEntrada() != null ? Time.valueOf(a.getHoraEntrada()) : null);
            ps.setTime(4, a.getHoraSalida() != null ? Time.valueOf(a.getHoraSalida()) : null);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                a.setId(rs.getLong(1)); // Guardamos el ID generado
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Error al registrar asistencia: " + e.getMessage());
            return false;
        }
    }

    // UPDATE salida
    public boolean registrarSalida(long asistenciaId, LocalTime horaSalida) {
        String sql = "UPDATE Asistencia SET horaSalida = ? WHERE id = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTime(1, Time.valueOf(horaSalida));
            ps.setLong(2, asistenciaId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al registrar salida: " + e.getMessage());
            return false;
        }
    }

    // LISTAR por usuario
    public List<Asistencia> listarPorUsuario(int usuarioId) {
        List<Asistencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM Asistencia WHERE usuario_id = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Asistencia(
                        rs.getLong("id"),
                        rs.getInt("usuario_id"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getTime("horaEntrada") != null ? rs.getTime("horaEntrada").toLocalTime() : null,
                        rs.getTime("horaSalida") != null ? rs.getTime("horaSalida").toLocalTime() : null
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar asistencias: " + e.getMessage());
        }
        return lista;
    }

    // NUEVO: LISTAR por usuario y fecha
    public List<Asistencia> listarPorUsuarioYFecha(int usuarioId, LocalDate fecha) {
        List<Asistencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM Asistencia WHERE usuario_id = ? AND fecha = ?";
        try (Connection con = Conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, usuarioId);
            ps.setDate(2, Date.valueOf(fecha));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Asistencia(
                        rs.getLong("id"),
                        rs.getInt("usuario_id"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getTime("horaEntrada") != null ? rs.getTime("horaEntrada").toLocalTime() : null,
                        rs.getTime("horaSalida") != null ? rs.getTime("horaSalida").toLocalTime() : null
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar asistencias por fecha: " + e.getMessage());
        }
        return lista;
    }
}



