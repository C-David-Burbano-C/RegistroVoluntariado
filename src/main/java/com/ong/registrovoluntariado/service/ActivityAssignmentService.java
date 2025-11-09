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
    public ActivityAssignment updateAssignment(Long id, ActivityAssignment updatedAssignment) {
        Optional<ActivityAssignment> existingOpt = assignmentRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("Assignment not found");
        }
        ActivityAssignment existing = existingOpt.get();

        if (updatedAssignment.getStartDate().isAfter(updatedAssignment.getEndDate())) {
            throw new RuntimeException("End date must be after start date");
        }

        existing.setStartDate(updatedAssignment.getStartDate());
        existing.setEndDate(updatedAssignment.getEndDate());
        existing.setStatus(updatedAssignment.getStatus());

        return assignmentRepository.save(existing);
    }
}