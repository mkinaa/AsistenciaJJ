package model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RolTest {

    private Rol instance;

    public RolTest() {
    }

    @Before
    public void setUp() {
        // Inicializa la instancia usando el constructor con parámetros
        instance = new Rol(1, "Admin", "Administrador");
    }

    @Test
    public void testGetId() {
        System.out.println("getId");
        assertEquals(1, instance.getId());
    }

    @Test
    public void testSetId() {
        System.out.println("setId");
        instance.setId(5);
        assertEquals(5, instance.getId());
    }

    @Test
    public void testGetNombre() {
        System.out.println("getNombre");
        assertEquals("Admin", instance.getNombre());
    }

    @Test
    public void testSetNombre() {
        System.out.println("setNombre");
        instance.setNombre("Usuario");
        assertEquals("Usuario", instance.getNombre());
    }

    @Test
    public void testGetDescripcion() {
        System.out.println("getDescripcion");
        assertEquals("Administrador", instance.getDescripcion());
    }

    @Test
    public void testSetDescripcion() {
        System.out.println("setDescripcion");
        instance.setDescripcion("Invitado");
        assertEquals("Invitado", instance.getDescripcion());
    }

    @Test
    public void testToString() {
        System.out.println("toString");
        // Ajusta esto según la implementación real de tu toString()
        String expResult = "Rol{id=1, nombre=Admin, descripcion=Administrador}";
        assertEquals(expResult, instance.toString());
    }
}

