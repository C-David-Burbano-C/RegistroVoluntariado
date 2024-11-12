package com.ong.registrovoluntariado.service;

import com.ong.registrovoluntariado.entity.Evaluation;
import com.ong.registrovoluntariado.entity.Volunteer;
import com.ong.registrovoluntariado.entity.Activity;
import com.ong.registrovoluntariado.entity.User;
import com.ong.registrovoluntariado.repository.EvaluationRepository;
import com.ong.registrovoluntariado.repository.VolunteerRepository;
import com.ong.registrovoluntariado.repository.ActivityRepository;
import com.ong.registrovoluntariado.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EvaluationService {

    @Autowired
    private EvaluationRepository evaluationRepository;

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserRepository userRepository;

    @PreAuthorize("hasRole('COORDINATOR') or hasRole('ADMIN')")
    public List<Evaluation> getAllEvaluations() {
        return evaluationRepository.findAll();
    }

    @PreAuthorize("hasRole('COORDINATOR') or hasRole('ADMIN')")
    public List<Evaluation> getEvaluationsByVolunteer(Long volunteerId) {
        return evaluationRepository.findByVolunteerId(volunteerId);
    }

    @PreAuthorize("hasRole('COORDINATOR') or hasRole('ADMIN')")
    public List<Evaluation> getEvaluationsByActivity(Long activityId) {
        return evaluationRepository.findByActivityId(activityId);
    }

    @PreAuthorize("hasRole('COORDINATOR') or hasRole('ADMIN')")
    public Optional<Evaluation> getEvaluationById(Long id) {
        return evaluationRepository.findById(id);
    }

    @PreAuthorize("hasRole('COORDINATOR') or hasRole('ADMIN')")
    @Transactional
    public Evaluation createEvaluation(Long volunteerId, Long activityId, Long evaluatorId, int rating, String comments) {
        Volunteer volunteer = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));
        User evaluator = userRepository.findById(evaluatorId)
                .orElseThrow(() -> new RuntimeException("Evaluator not found"));

        Evaluation evaluation = new Evaluation();
        evaluation.setVolunteer(volunteer);
        evaluation.setActivity(activity);
        evaluation.setEvaluator(evaluator);
        evaluation.setRating(rating);
        evaluation.setComments(comments);
        evaluation.setEvaluationDate(LocalDateTime.now());

        return evaluationRepository.save(evaluation);
    }

    @PreAuthorize("hasRole('COORDINATOR') or hasRole('ADMIN')")
    @Transactional
    public Evaluation updateEvaluation(Long id, int rating, String comments) {
        Evaluation evaluation = evaluationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evaluation not found"));

        evaluation.setRating(rating);
        evaluation.setComments(comments);
        evaluation.setEvaluationDate(LocalDateTime.now());

        return evaluationRepository.save(evaluation);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void deleteEvaluation(Long id) {
        if (!evaluationRepository.existsById(id)) {
            throw new RuntimeException("Evaluation not found");
        }
        evaluationRepository.deleteById(id);
    }

    @PreAuthorize("hasRole('COORDINATOR') or hasRole('ADMIN')")
    public double getAverageRatingByVolunteer(Long volunteerId) {
        List<Evaluation> evaluations = evaluationRepository.findByVolunteerId(volunteerId);
        if (evaluations.isEmpty()) {
            return 0.0;
        }
        return evaluations.stream()
                .mapToInt(Evaluation::getRating)
                .average()
                .orElse(0.0);
    }
}