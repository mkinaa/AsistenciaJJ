package model;

import org.junit.*;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.*;

public class AsistenciaTest {

    private Asistencia instance;

    @Before
    public void setUp() {
        // Creamos una instancia válida antes de cada test
        instance = new Asistencia(
                1,                  // id
                10,                 // usuarioId
                LocalDate.of(2025, 9, 10), // fecha
                LocalTime.of(9, 0), // horaEntrada
                LocalTime.of(17, 0) // horaSalida
        );
    }

    @Test
    public void testGetId() {
        assertEquals(1, instance.getId());
    }

    @Test
    public void testSetId() {
        instance.setId(99);
        assertEquals(99, instance.getId());
    }

    @Test
    public void testGetUsuarioId() {
        assertEquals(10, instance.getUsuarioId());
    }

    @Test
    public void testSetUsuarioId() {
        instance.setUsuarioId(20);
        assertEquals(20, instance.getUsuarioId());
    }

    @Test
    public void testGetFecha() {
        assertEquals(LocalDate.of(2025, 9, 10), instance.getFecha());
    }

    @Test
    public void testSetFecha() {
        LocalDate nuevaFecha = LocalDate.of(2025, 12, 25);
        instance.setFecha(nuevaFecha);
        assertEquals(nuevaFecha, instance.getFecha());
    }

    @Test
    public void testGetHoraEntrada() {
        assertEquals(LocalTime.of(9, 0), instance.getHoraEntrada());
    }

    @Test
    public void testSetHoraEntrada() {
        LocalTime nueva = LocalTime.of(8, 30);
        instance.setHoraEntrada(nueva);
        assertEquals(nueva, instance.getHoraEntrada());
    }

    @Test
    public void testGetHoraSalida() {
        assertEquals(LocalTime.of(17, 0), instance.getHoraSalida());
    }

    @Test
    public void testSetHoraSalida() {
        LocalTime nueva = LocalTime.of(18, 0);
        instance.setHoraSalida(nueva);
        assertEquals(nueva, instance.getHoraSalida());
    }

    @Test
    public void testToString() {
        String texto = instance.toString();
        assertNotNull(texto);
        assertTrue(texto.contains("10")); // debería contener el usuarioId
    }
}

