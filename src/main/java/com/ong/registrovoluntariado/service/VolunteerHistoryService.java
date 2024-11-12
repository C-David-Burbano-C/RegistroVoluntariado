package com.ong.registrovoluntariado.service;

import com.ong.registrovoluntariado.entity.ActivityAssignment;
import com.ong.registrovoluntariado.entity.Attendance;
import com.ong.registrovoluntariado.repository.ActivityAssignmentRepository;
import com.ong.registrovoluntariado.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VolunteerHistoryService {

    @Autowired
    private ActivityAssignmentRepository assignmentRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    public List<ActivityAssignment> getVolunteerHistory(Long volunteerId) {
        return assignmentRepository.findByVolunteerId(volunteerId);
    }

    public Map<String, Object> getVolunteerHistorySummary(Long volunteerId) {
        List<ActivityAssignment> assignments = getVolunteerHistory(volunteerId);
        long totalActivities = assignments.size();
        long completedActivities = assignments.stream()
            .filter(a -> "COMPLETED".equals(a.getStatus()))
            .count();
        double totalHours = assignments.stream()
            .mapToDouble(a -> {
                List<Attendance> attendances = attendanceRepository.findByAssignmentId(a.getId());
                return attendances.stream()
                    .filter(att -> "CONFIRMED".equals(att.getStatus()))
                    .mapToDouble(att -> {
                        if (att.getCheckInTime() != null && att.getCheckOutTime() != null) {
                            return java.time.Duration.between(att.getCheckInTime(), att.getCheckOutTime()).toHours();
                        }
                        return 0;
                    })
                    .sum();
            })
            .sum();

        return Map.of(
            "totalActivities", totalActivities,
            "completedActivities", completedActivities,
            "totalHours", totalHours
        );
    }
}