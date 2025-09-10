package model;

import org.junit.*;
import static org.junit.Assert.*;
import javax.swing.table.DefaultTableModel;
import java.io.File;

import model.ReportePDF; 

public class ReportePDFTest {
    
    private DefaultTableModel model;
    private String rutaArchivo;

    @Before
    public void setUp() {
        String[] columnas = {"ID Usuario", "Nombre", "Fecha", "Hora"};
        model = new DefaultTableModel(columnas, 0);
        model.addRow(new Object[]{1, "EmpleadoTest", "2025-09-10", "09:35:00"});
        model.addRow(new Object[]{2, "EmpleadoDos", "2025-09-10", "08:55:00"});

        // Usar carpeta temporal de forma segura
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        rutaArchivo = new File(tempDir, "reporte_test.pdf").getAbsolutePath();
    }

    @Test
    public void testExportar() throws Exception {
        ReportePDF.exportar("Reporte de Prueba", model, rutaArchivo);

        File archivo = new File(rutaArchivo);
        assertTrue("El archivo PDF debe haberse creado", archivo.exists());
        assertTrue("El archivo PDF no debe estar vacío", archivo.length() > 0);

        if (archivo.exists()) {
            archivo.delete();
        }
    }
}

