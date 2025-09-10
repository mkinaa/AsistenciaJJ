package dao;

import model.Rol;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RolDAO {
    private Connection con;

    public RolDAO(Connection con) {
        this.con = con;
    }

    public void insertar(Rol rol) throws SQLException {
        String sql = "INSERT INTO Rol (nombre, descripcion) VALUES (?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, rol.getNombre());
            ps.setString(2, rol.getDescripcion());
            ps.executeUpdate();
        }
    }

    public List<Rol> listar() throws SQLException {
        List<Rol> lista = new ArrayList<>();
        String sql = "SELECT * FROM Rol";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Rol(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion")));
            }
        }
        return lista;
    }
}

