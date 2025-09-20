package model;

import org.junit.*;
import java.time.LocalDate;

import static org.junit.Assert.*;

public class ReporteTest {

    private Reporte instance;

    @Before
    public void setUp() {
        // Creamos un objeto válido de Reporte antes de cada test
        instance = new Reporte(
                1,                       // id
                "atrasos",               // tipo
                LocalDate.of(2025, 9, 10), // fechaGeneracion
                100                      // administradorId
        );
    }

    @Test
    public void testGetId() {
        assertEquals(1, instance.getId());
    }

    @Test
    public void testSetId() {
        instance.setId(50);
        assertEquals(50, instance.getId());
    }

    @Test
    public void testGetTipo() {
        assertEquals("atrasos", instance.getTipo());
    }

    @Test
    public void testSetTipo() {
        instance.setTipo("inasistencias");
        assertEquals("inasistencias", instance.getTipo());
    }

    @Test
    public void testGetFechaGeneracion() {
        assertEquals(LocalDate.of(2025, 9, 10), instance.getFechaGeneracion());
    }

    @Test
    public void testSetFechaGeneracion() {
        LocalDate nueva = LocalDate.of(2025, 12, 25);
        instance.setFechaGeneracion(nueva);
        assertEquals(nueva, instance.getFechaGeneracion());
    }

    @Test
    public void testGetAdministradorId() {
        assertEquals(100, instance.getAdministradorId());
    }

    @Test
    public void testSetAdministradorId() {
        instance.setAdministradorId(200);
        assertEquals(200, instance.getAdministradorId());
    }

    @Test
    public void testToString() {
        String texto = instance.toString();
        assertNotNull(texto);
        assertTrue(texto.contains("atrasos")); // debería contener el tipo
    }
}

