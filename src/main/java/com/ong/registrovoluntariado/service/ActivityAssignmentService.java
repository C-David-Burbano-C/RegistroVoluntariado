package com.ong.registrovoluntariado.service;

import com.ong.registrovoluntariado.entity.Activity;
import com.ong.registrovoluntariado.entity.ActivityAssignment;
import com.ong.registrovoluntariado.entity.User;
import com.ong.registrovoluntariado.entity.Volunteer;
import com.ong.registrovoluntariado.repository.ActivityAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityAssignmentService {

    @Autowired
    private ActivityAssignmentRepository assignmentRepository;

    @Autowired
    private VolunteerService volunteerService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private NotificationService notificationService;

    @Transactional
    public ActivityAssignment assignVolunteerToActivity(Long volunteerId, Long activityId,
                                                       LocalDate startDate, LocalDate endDate, User assignedBy) {
        if (startDate.isAfter(endDate)) {
            throw new RuntimeException("End date must be after start date");
        }

        Optional<Volunteer> volunteerOpt = volunteerService.getVolunteerById(volunteerId);
        Optional<Activity> activityOpt = activityService.getActivityById(activityId);

        if (volunteerOpt.isEmpty() || activityOpt.isEmpty()) {
            throw new RuntimeException("Volunteer or Activity not found");
        }

        ActivityAssignment assignment = new ActivityAssignment();
        assignment.setVolunteer(volunteerOpt.get());
        assignment.setActivity(activityOpt.get());
        assignment.setStartDate(startDate);
        assignment.setEndDate(endDate);
        assignment.setAssignedBy(assignedBy);

        ActivityAssignment saved = assignmentRepository.save(assignment);

        // Send notification
        notificationService.sendActivityAssignmentNotification(saved);

        return saved;
    }

    public List<ActivityAssignment> getAssignmentsByVolunteer(Long volunteerId) {
        return assignmentRepository.findByVolunteerId(volunteerId);
    }

    public List<ActivityAssignment> getAssignmentsByActivity(Long activityId) {
        return assignmentRepository.findByActivityId(activityId);
    }

    @Transactional
    public ActivityAssignment cancelAssignment(Long assignmentId, String reason, User cancelledBy) {
        Optional<ActivityAssignment> assignmentOpt = assignmentRepository.findById(assignmentId);
        if (assignmentOpt.isEmpty()) {
            throw new RuntimeException("Assignment not found");
        }

        ActivityAssignment assignment = assignmentOpt.get();

        // Validar que la actividad no haya empezado
        if (assignment.getActivity().getStartDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Cannot cancel assignment for activity that has already started");
        }

        // Validar que no esté ya cancelada
        if ("CANCELLED".equals(assignment.getStatus())) {
            throw new RuntimeException("Assignment is already cancelled");
        }

        assignment.setStatus("CANCELLED");
        assignment.setCancellationReason(reason);
        assignment.setCancelledBy(cancelledBy);
        assignment.setCancellationDate(LocalDate.now());

        ActivityAssignment saved = assignmentRepository.save(assignment);

        // Send cancellation notification
        notificationService.sendAssignmentCancellationNotification(saved);

        return saved;
    }

    public List<ActivityAssignment> getCancelledAssignments() {
        return assignmentRepository.findByStatus("CANCELLED");
    }

    public List<ActivityAssignment> getActiveAssignments() {
        return assignmentRepository.findByStatusNot("CANCELLED");
    }
}