package com.ong.registrovoluntariado.controller;

import com.ong.registrovoluntariado.entity.Certificate;
import com.ong.registrovoluntariado.entity.User;
import com.ong.registrovoluntariado.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @PostMapping
    @PreAuthorize("hasRole('COORDINATOR') or hasRole('ADMIN')")
    public ResponseEntity<Certificate> generateCertificate(@RequestBody CertificateRequest request,
                                                         @AuthenticationPrincipal User user) {
        try {
            Certificate certificate = certificateService.generateCertificate(
                request.getVolunteerId(),
                request.getActivityId(),
                request.getHoursWorked(),
                user
            );
            return ResponseEntity.ok(certificate);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/volunteer/{volunteerId}")
    public ResponseEntity<List<Certificate>> getCertificatesByVolunteer(@PathVariable Long volunteerId) {
        List<Certificate> certificates = certificateService.getCertificatesByVolunteer(volunteerId);
        return ResponseEntity.ok(certificates);
    }

    @GetMapping("/verify/{code}")
    public ResponseEntity<Certificate> verifyCertificate(@PathVariable String code) {
        return certificateService.getCertificateByCode(code)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    public static class CertificateRequest {
        private Long volunteerId;
        private Long activityId;
        private Double hoursWorked;

        // Getters and setters
        public Long getVolunteerId() { return volunteerId; }
        public void setVolunteerId(Long volunteerId) { this.volunteerId = volunteerId; }
        public Long getActivityId() { return activityId; }
        public void setActivityId(Long activityId) { this.activityId = activityId; }
        public Double getHoursWorked() { return hoursWorked; }
        public void setHoursWorked(Double hoursWorked) { this.hoursWorked = hoursWorked; }
    }
}