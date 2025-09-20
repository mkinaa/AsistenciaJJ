/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UsuarioTest {

    private Usuario instance;

    public UsuarioTest() {
    }

    @Before
    public void setUp() {
        // Inicializa la instancia con valores de prueba
        instance = new Usuario(1, "Juan", "juan@ejemplo.com", "1234", 2);
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
        assertEquals("Juan", instance.getNombre());
    }

    @Test
    public void testSetNombre() {
        System.out.println("setNombre");
        instance.setNombre("Pedro");
        assertEquals("Pedro", instance.getNombre());
    }

    @Test
    public void testGetCorreo() {
        System.out.println("getCorreo");
        assertEquals("juan@ejemplo.com", instance.getCorreo());
    }

    @Test
    public void testSetCorreo() {
        System.out.println("setCorreo");
        instance.setCorreo("pedro@ejemplo.com");
        assertEquals("pedro@ejemplo.com", instance.getCorreo());
    }

    @Test
    public void testGetContrasena() {
        System.out.println("getContrasena");
        assertEquals("1234", instance.getContrasena());
    }

    @Test
    public void testSetContrasena() {
        System.out.println("setContrasena");
        instance.setContrasena("abcd");
        assertEquals("abcd", instance.getContrasena());
    }

    @Test
    public void testGetRolId() {
        System.out.println("getRolId");
        assertEquals(2, instance.getRolId());
    }

    @Test
    public void testSetRolId() {
        System.out.println("setRolId");
        instance.setRolId(3);
        assertEquals(3, instance.getRolId());
    }
}
