package dao;

import model.Usuario;
import org.junit.*;
import java.sql.*;
import java.util.List;
import static org.junit.Assert.*;

public class UsuarioDAOTest {

    private static Connection con;
    private static UsuarioDAO usuarioDAO;

    @BeforeClass
    public static void setUpClass() throws Exception {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/controlasistencia", "root", "");
        usuarioDAO = new UsuarioDAO();
    }

    @Test
    public void testInsertarYObtener() {
        Usuario nuevo = new Usuario(0, "TestUser", "test@correo.com", "1234", 1);
        assertTrue(usuarioDAO.insertar(nuevo));

        Usuario obtenido = usuarioDAO.obtenerPorCorreo("test@correo.com");
        assertNotNull(obtenido);
        assertEquals("TestUser", obtenido.getNombre());
    }

    @Test
    public void testActualizar() {
        Usuario nuevo = new Usuario(0, "UpdateUser", "update@correo.com", "1234", 1);
        usuarioDAO.insertar(nuevo);

        Usuario u = usuarioDAO.obtenerPorCorreo("update@correo.com");
        assertNotNull(u);

        u.setNombre("UserModificado");
        assertTrue(usuarioDAO.actualizar(u));

        Usuario modificado = usuarioDAO.obtenerPorCorreo("update@correo.com");
        assertEquals("UserModificado", modificado.getNombre());
    }

    @Test
    public void testListar() {
        List<Usuario> lista = usuarioDAO.listar();
        assertNotNull(lista);
        assertTrue(lista.size() > 0);
    }

    @Test
    public void testEliminar() {
        Usuario u = usuarioDAO.obtenerPorCorreo("test@correo.com");
        assertNotNull(u);
        assertTrue(usuarioDAO.eliminar(u.getId()));
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        con.close();
    }
}
