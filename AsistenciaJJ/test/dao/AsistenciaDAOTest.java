package dao;

import model.Asistencia;
import model.Usuario;
import org.junit.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.Assert.*;

public class AsistenciaDAOTest {

    private static Connection con;
    private static AsistenciaDAO asistenciaDAO;
    private static UsuarioDAO usuarioDAO;
    private static Usuario empleadoTest;

    @BeforeClass
    public static void setUpClass() throws Exception {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/controlasistencia", "root", "");
        asistenciaDAO = new AsistenciaDAO();
        usuarioDAO = new UsuarioDAO();

        // Crear un empleado temporal para las pruebas
        Usuario temp = usuarioDAO.obtenerPorCorreo("empleado_test@correo.com");
        if (temp == null) {
            usuarioDAO.insertar(new Usuario(0, "EmpleadoTest", "empleado_test@correo.com", "1234", 1));
            temp = usuarioDAO.obtenerPorCorreo("empleado_test@correo.com");
        }
        empleadoTest = temp;
    }

    @Test
    public void testRegistrarEntradaYSalida() {
        Asistencia a = new Asistencia(0, empleadoTest.getId(), LocalDate.now(), LocalTime.of(10, 0), null);
        assertTrue(asistenciaDAO.registrarEntrada(a));

        // Registrar salida
        assertTrue(asistenciaDAO.registrarSalida(a.getId(), LocalTime.of(17, 0)));
    }

    @Test
    public void testListarPorUsuario() {
        List<Asistencia> lista = asistenciaDAO.listarPorUsuario(empleadoTest.getId());
        assertNotNull(lista);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        // Opcional: eliminar empleado de prueba
        if (empleadoTest != null) {
            usuarioDAO.eliminar(empleadoTest.getId());
        }
        con.close();
    }
}


