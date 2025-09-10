/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author jvanj
 */
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javax.swing.table.DefaultTableModel;
import java.io.FileOutputStream;

public class ReportePDF {

    public static void exportar(String titulo, DefaultTableModel model, String rutaArchivo) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(rutaArchivo));
        document.open();

        // Título
        Paragraph header = new Paragraph(titulo, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18));
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);
        document.add(new Paragraph(" "));

        // Tabla PDF
        PdfPTable table = new PdfPTable(model.getColumnCount());
        for (int i = 0; i < model.getColumnCount(); i++) {
            table.addCell(new PdfPCell(new Phrase(model.getColumnName(i))));
        }

        for (int row = 0; row < model.getRowCount(); row++) {
            for (int col = 0; col < model.getColumnCount(); col++) {
                Object val = model.getValueAt(row, col);
                table.addCell(val != null ? val.toString() : "-");
            }
        }

        document.add(table);
        document.close();
    }
}