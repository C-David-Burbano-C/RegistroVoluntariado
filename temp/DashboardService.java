package com.ong.registrovoluntariado.service;

import com.ong.registrovoluntariado.repository.ActivityAssignmentRepository;
import com.ong.registrovoluntariado.repository.ActivityRepository;
import com.ong.registrovoluntariado.repository.AttendanceRepository;
import com.ong.registrovoluntariado.repository.VolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardService {

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityAssignmentRepository assignmentRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    public Map<String, Object> getDashboardData() {
        Map<String, Object> data = new HashMap<>();

        // Total volunteers
        data.put("totalVolunteers", volunteerRepository.count());

        // Active activities
        data.put("activeActivities", activityRepository.findByActiveTrue().size());

        // Completed activities this month
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        // This would need a custom query, for simplicity:
        data.put("completedActivitiesThisMonth", 0); // Placeholder

        // Attendance stats (placeholder)
        data.put("averageAttendance", 85.5);

        return data;
    }
}