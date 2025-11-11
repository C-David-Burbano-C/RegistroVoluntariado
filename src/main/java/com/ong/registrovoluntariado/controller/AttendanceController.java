package com.ong.registrovoluntariado.controller;

import com.ong.registrovoluntariado.entity.Attendance;
import com.ong.registrovoluntariado.entity.User;
import com.ong.registrovoluntariado.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping
    @PreAuthorize("hasRole('COORDINATOR') or hasRole('ADMIN')")
    public ResponseEntity<Attendance> recordAttendance(@RequestBody AttendanceRequest request,
                                                      @AuthenticationPrincipal User user) {
        try {
            Attendance attendance = attendanceService.recordAttendance(
                request.getAssignmentId(),
                request.getDate(),
                request.getStatus(),
                request.getCheckInTime(),
                request.getCheckOutTime(),
                request.getObservations(),
                request.getPerformanceLevel(),
                user
            );
            return ResponseEntity.ok(attendance);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/assignment/{assignmentId}")
    @PreAuthorize("hasRole('COORDINATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<Attendance>> getAttendanceByAssignment(@PathVariable Long assignmentId) {
        List<Attendance> attendances = attendanceService.getAttendanceByAssignment(assignmentId);
        return ResponseEntity.ok(attendances);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('COORDINATOR') or hasRole('ADMIN')")
    public ResponseEntity<Attendance> updateAttendance(@PathVariable Long id, @Valid @RequestBody Attendance attendance) {
        try {
            Attendance updated = attendanceService.updateAttendance(id, attendance);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    public static class AttendanceRequest {
        private Long assignmentId;
        private LocalDate date;
        private Attendance.Status status;
        private LocalTime checkInTime;
        private LocalTime checkOutTime;
        private String observations;
        private Attendance.PerformanceLevel performanceLevel;

        // Getters and setters
        public Long getAssignmentId() { return assignmentId; }
        public void setAssignmentId(Long assignmentId) { this.assignmentId = assignmentId; }
        public LocalDate getDate() { return date; }
        public void setDate(LocalDate date) { this.date = date; }
        public Attendance.Status getStatus() { return status; }
        public void setStatus(Attendance.Status status) { this.status = status; }
        public LocalTime getCheckInTime() { return checkInTime; }
        public void setCheckInTime(LocalTime checkInTime) { this.checkInTime = checkInTime; }
        public LocalTime getCheckOutTime() { return checkOutTime; }
        public void setCheckOutTime(LocalTime checkOutTime) { this.checkOutTime = checkOutTime; }
        public String getObservations() { return observations; }
        public void setObservations(String observations) { this.observations = observations; }
        public Attendance.PerformanceLevel getPerformanceLevel() { return performanceLevel; }
        public void setPerformanceLevel(Attendance.PerformanceLevel performanceLevel) { this.performanceLevel = performanceLevel; }
    }
}