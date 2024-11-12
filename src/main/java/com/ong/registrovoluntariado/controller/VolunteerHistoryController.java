package com.ong.registrovoluntariado.controller;

import com.ong.registrovoluntariado.entity.ActivityAssignment;
import com.ong.registrovoluntariado.service.VolunteerHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/volunteers/{volunteerId}/history")
public class VolunteerHistoryController {

    @Autowired
    private VolunteerHistoryService historyService;

    @GetMapping
    @PreAuthorize("hasRole('COORDINATOR') or hasRole('ADMIN') or @userService.isCurrentUserVolunteer(#volunteerId)")
    public ResponseEntity<List<ActivityAssignment>> getVolunteerHistory(@PathVariable Long volunteerId) {
        List<ActivityAssignment> history = historyService.getVolunteerHistory(volunteerId);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/summary")
    @PreAuthorize("hasRole('COORDINATOR') or hasRole('ADMIN') or @userService.isCurrentUserVolunteer(#volunteerId)")
    public ResponseEntity<Map<String, Object>> getVolunteerHistorySummary(@PathVariable Long volunteerId) {
        Map<String, Object> summary = historyService.getVolunteerHistorySummary(volunteerId);
        return ResponseEntity.ok(summary);
    }
}