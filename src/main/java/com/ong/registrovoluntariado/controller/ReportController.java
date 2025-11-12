package com.ong.registrovoluntariado.controller;

import com.ong.registrovoluntariado.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/volunteers/pdf")
    @PreAuthorize("hasRole('COORDINATOR') or hasRole('ADMIN')")
    public ResponseEntity<byte[]> getVolunteerReportPDF() {
        try {
            byte[] pdf = reportService.generateVolunteerReportPDF();
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=volunteers.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/volunteers/excel")
    @PreAuthorize("hasRole('COORDINATOR') or hasRole('ADMIN')")
    public ResponseEntity<byte[]> getVolunteerReportExcel() {
        try {
            byte[] excel = reportService.generateVolunteerReportExcel();
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=volunteers.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excel);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}