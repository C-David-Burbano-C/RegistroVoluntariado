package com.ong.registrovoluntariado.service;

import com.ong.registrovoluntariado.entity.Volunteer;
import com.ong.registrovoluntariado.repository.VolunteerRepository;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private VolunteerRepository volunteerRepository;

    public byte[] generateVolunteerReportPDF() throws IOException {
        List<Volunteer> volunteers = volunteerRepository.findAll();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("Volunteer Report"));

        Table table = new Table(4);
        table.addHeaderCell("Name");
        table.addHeaderCell("Email");
        table.addHeaderCell("Phone");
        table.addHeaderCell("Registration Date");

        for (Volunteer v : volunteers) {
            table.addCell(v.getFullName());
            table.addCell(v.getEmail());
            table.addCell(v.getPhone());
            table.addCell(v.getRegistrationDate().toString());
        }

        document.add(table);
        document.close();

        return baos.toByteArray();
    }

    public byte[] generateVolunteerReportExcel() throws IOException {
        List<Volunteer> volunteers = volunteerRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Volunteers");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Name");
        headerRow.createCell(1).setCellValue("Email");
        headerRow.createCell(2).setCellValue("Phone");
        headerRow.createCell(3).setCellValue("Registration Date");

        int rowNum = 1;
        for (Volunteer v : volunteers) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(v.getFullName());
            row.createCell(1).setCellValue(v.getEmail());
            row.createCell(2).setCellValue(v.getPhone());
            row.createCell(3).setCellValue(v.getRegistrationDate().toString());
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();

        return baos.toByteArray();
    }
}