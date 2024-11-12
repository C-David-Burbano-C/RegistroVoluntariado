package com.ong.registrovoluntariado.controller;

import com.ong.registrovoluntariado.entity.Evaluation;
import com.ong.registrovoluntariado.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/evaluations")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    @GetMapping
    public ResponseEntity<List<Evaluation>> getAllEvaluations() {
        List<Evaluation> evaluations = evaluationService.getAllEvaluations();
        return ResponseEntity.ok(evaluations);
    }

    @GetMapping("/volunteer/{volunteerId}")
    public ResponseEntity<List<Evaluation>> getEvaluationsByVolunteer(@PathVariable Long volunteerId) {
        List<Evaluation> evaluations = evaluationService.getEvaluationsByVolunteer(volunteerId);
        return ResponseEntity.ok(evaluations);
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<List<Evaluation>> getEvaluationsByActivity(@PathVariable Long activityId) {
        List<Evaluation> evaluations = evaluationService.getEvaluationsByActivity(activityId);
        return ResponseEntity.ok(evaluations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evaluation> getEvaluationById(@PathVariable Long id) {
        Optional<Evaluation> evaluation = evaluationService.getEvaluationById(id);
        return evaluation.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Evaluation> createEvaluation(
            @RequestParam Long volunteerId,
            @RequestParam Long activityId,
            @RequestParam Long evaluatorId,
            @RequestParam int rating,
            @RequestParam(required = false) String comments) {
        try {
            Evaluation evaluation = evaluationService.createEvaluation(volunteerId, activityId, evaluatorId, rating, comments);
            return ResponseEntity.ok(evaluation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evaluation> updateEvaluation(
            @PathVariable Long id,
            @RequestParam int rating,
            @RequestParam(required = false) String comments) {
        try {
            Evaluation evaluation = evaluationService.updateEvaluation(id, rating, comments);
            return ResponseEntity.ok(evaluation);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvaluation(@PathVariable Long id) {
        try {
            evaluationService.deleteEvaluation(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/volunteer/{volunteerId}/average")
    public ResponseEntity<Double> getAverageRatingByVolunteer(@PathVariable Long volunteerId) {
        double average = evaluationService.getAverageRatingByVolunteer(volunteerId);
        return ResponseEntity.ok(average);
    }
}