package com.ong.registrovoluntariado.controller;

import com.ong.registrovoluntariado.entity.Activity;
import com.ong.registrovoluntariado.entity.User;
import com.ong.registrovoluntariado.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @PostMapping
    @PreAuthorize("hasRole('COORDINATOR') or hasRole('ADMIN')")
    public ResponseEntity<Activity> createActivity(@Valid @RequestBody Activity activity,
                                                  @AuthenticationPrincipal User user) {
        try {
            Activity saved = activityService.createActivity(activity, user);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<Activity>> getAllActiveActivities() {
        List<Activity> activities = activityService.getAllActiveActivities();
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Activity>> getActivitiesByType(@PathVariable String type) {
        List<Activity> activities = activityService.getActivitiesByType(type);
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Activity> getActivityById(@PathVariable Long id) {
        return activityService.getActivityById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('COORDINATOR') or hasRole('ADMIN')")
    public ResponseEntity<Activity> updateActivity(@PathVariable Long id, @Valid @RequestBody Activity activity) {
        try {
            Activity updated = activityService.updateActivity(id, activity);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('COORDINATOR') or hasRole('ADMIN')")
    public ResponseEntity<Void> deactivateActivity(@PathVariable Long id) {
        activityService.deactivateActivity(id);
        return ResponseEntity.noContent().build();
    }
}