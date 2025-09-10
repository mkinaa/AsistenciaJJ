package dao;

import model.Rol;
import org.junit.*;
import java.sql.*;
import java.util.List;
import static org.junit.Assert.*;

public class RolDAOTest {

    private static Connection con;
    private static RolDAO rolDAO;

    @BeforeClass
    public static void setUpClass() throws Exception {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/controlasistencia", "root", "");
        rolDAO = new RolDAO(con);
    }

    @Test
    public void testListarRoles() throws Exception {
        List<Rol> lista = rolDAO.listar();
        assertNotNull(lista);
        assertTrue(lista.size() >= 2); // Empleado y Admin
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        con.close();
    }
}

