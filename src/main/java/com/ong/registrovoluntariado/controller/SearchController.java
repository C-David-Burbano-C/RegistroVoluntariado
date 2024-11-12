package com.ong.registrovoluntariado.controller;

import com.ong.registrovoluntariado.entity.Activity;
import com.ong.registrovoluntariado.entity.Volunteer;
import com.ong.registrovoluntariado.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/volunteers")
    public ResponseEntity<List<Volunteer>> searchVolunteers(@RequestParam String query) {
        List<Volunteer> results = searchService.searchVolunteers(query);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/activities")
    public ResponseEntity<List<Activity>> searchActivitiesByType(@RequestParam String type) {
        List<Activity> results = searchService.searchActivitiesByType(type);
        return ResponseEntity.ok(results);
    }
}