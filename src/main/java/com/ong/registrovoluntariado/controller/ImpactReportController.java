package com.ong.registrovoluntariado.controller;

import com.ong.registrovoluntariado.service.ImpactReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/reports/impact")
public class ImpactReportController {

    @Autowired
    private ImpactReportService impactReportService;

    @GetMapping("/general")
    public ResponseEntity<Map<String, Object>> getGeneralImpactReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            Map<String, Object> report = impactReportService.generateImpactReport(startDate, endDate);
            return ResponseEntity.ok(report);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/volunteer/{volunteerId}")
    public ResponseEntity<Map<String, Object>> getVolunteerImpactReport(@PathVariable Long volunteerId) {
        try {
            Map<String, Object> report = impactReportService.generateVolunteerImpactReport(volunteerId);
            return ResponseEntity.ok(report);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}