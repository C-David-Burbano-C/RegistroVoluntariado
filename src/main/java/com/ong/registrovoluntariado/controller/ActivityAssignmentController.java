package com.ong.registrovoluntariado.controller;

import com.ong.registrovoluntariado.entity.ActivityAssignment;
import com.ong.registrovoluntariado.entity.User;
import com.ong.registrovoluntariado.service.ActivityAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/assignments")
public class ActivityAssignmentController {

    @Autowired
    private ActivityAssignmentService assignmentService;

    @PostMapping
    @PreAuthorize("hasRole('COORDINATOR') or hasRole('ADMIN')")
    public ResponseEntity<ActivityAssignment> assignVolunteer(@RequestBody AssignmentRequest request,
                                                             @AuthenticationPrincipal User user) {
        try {
            ActivityAssignment assignment = assignmentService.assignVolunteerToActivity(
                request.getVolunteerId(),
                request.getActivityId(),
                request.getStartDate(),
                request.getEndDate(),
                user
            );
            return ResponseEntity.ok(assignment);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/volunteer/{volunteerId}")
    @PreAuthorize("hasRole('COORDINATOR') or hasRole('ADMIN') or @userService.isCurrentUserVolunteer(#volunteerId)")
    public ResponseEntity<List<ActivityAssignment>> getAssignmentsByVolunteer(@PathVariable Long volunteerId) {
        List<ActivityAssignment> assignments = assignmentService.getAssignmentsByVolunteer(volunteerId);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/activity/{activityId}")
    @PreAuthorize("hasRole('COORDINATOR') or hasRole('ADMIN')")
    public ResponseEntity<List<ActivityAssignment>> getAssignmentsByActivity(@PathVariable Long activityId) {
        List<ActivityAssignment> assignments = assignmentService.getAssignmentsByActivity(activityId);
        return ResponseEntity.ok(assignments);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('COORDINATOR') or hasRole('ADMIN')")
    public ResponseEntity<ActivityAssignment> updateAssignment(@PathVariable Long id,
                                                              @Valid @RequestBody ActivityAssignment assignment) {
        try {
            ActivityAssignment updated = assignmentService.updateAssignment(id, assignment);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    public static class AssignmentRequest {
        private Long volunteerId;
        private Long activityId;
        private LocalDate startDate;
        private LocalDate endDate;

        // Getters and setters
        public Long getVolunteerId() { return volunteerId; }
        public void setVolunteerId(Long volunteerId) { this.volunteerId = volunteerId; }
        public Long getActivityId() { return activityId; }
        public void setActivityId(Long activityId) { this.activityId = activityId; }
        public LocalDate getStartDate() { return startDate; }
        public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
        public LocalDate getEndDate() { return endDate; }
        public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    }
}