package bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/ControlAsistencia";
    private static final String USER = "root"; // cámbialo si usas otro usuario
    private static final String PASS = "";     // cámbialo si tienes contraseña

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void main(String[] args) {
        try (Connection con = getConnection()) {
            if (con != null) {
                System.out.println("✅ Conexión exitosa a la base de datos");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error de conexión: " + e.getMessage());
        }
    }
    }
