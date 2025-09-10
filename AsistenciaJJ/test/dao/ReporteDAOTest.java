package dao;

import model.Reporte;
import model.Usuario;
import org.junit.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class ReporteDAOTest {

    private static Connection con;
    private static ReporteDAO reporteDAO;
    private static UsuarioDAO usuarioDAO;
    private static Usuario admin;

    @BeforeClass
    public static void setUpClass() throws Exception {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/controlasistencia", "root", "");
        reporteDAO = new ReporteDAO(con);
        usuarioDAO = new UsuarioDAO();

        // Crear un admin temporal solo para pruebas
        Usuario temp = usuarioDAO.obtenerPorCorreo("admin_test@correo.com");
        if (temp == null) {
            usuarioDAO.insertar(new Usuario(0, "AdminTest", "admin_test@correo.com", "1234", 2));
            temp = usuarioDAO.obtenerPorCorreo("admin_test@correo.com");
        }
        admin = temp;
    }

    @Test
    public void testInsertarReporte() throws Exception {
        Reporte r = new Reporte(0, "atrasos", LocalDate.now(), admin.getId());
        reporteDAO.insertar(r);
        List<Reporte> lista = reporteDAO.listar();
        assertTrue(lista.size() > 0);
    }

    @Test
    public void testObtenerAtrasos() throws Exception {
        List<Object[]> atrasos = reporteDAO.obtenerAtrasos();
        assertNotNull(atrasos);
    }

    @Test
    public void testObtenerSalidasAnticipadas() throws Exception {
        List<Object[]> salidas = reporteDAO.obtenerSalidasAnticipadas();
        assertNotNull(salidas);
    }

    @Test
    public void testObtenerInasistencias() throws Exception {
        List<Object[]> inasistencias = reporteDAO.obtenerInasistencias();
        assertNotNull(inasistencias);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        // Opcional: limpiar admin de prueba
        if (admin != null) {
            usuarioDAO.eliminar(admin.getId());
        }
        con.close();
    }
}
