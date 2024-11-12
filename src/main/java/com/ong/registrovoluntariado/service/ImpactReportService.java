package com.ong.registrovoluntariado.service;

import com.ong.registrovoluntariado.entity.Activity;
import com.ong.registrovoluntariado.entity.ActivityAssignment;
import com.ong.registrovoluntariado.entity.Attendance;
import com.ong.registrovoluntariado.entity.Volunteer;
import com.ong.registrovoluntariado.repository.ActivityAssignmentRepository;
import com.ong.registrovoluntariado.repository.ActivityRepository;
import com.ong.registrovoluntariado.repository.AttendanceRepository;
import com.ong.registrovoluntariado.repository.VolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ImpactReportService {

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityAssignmentRepository activityAssignmentRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @PreAuthorize("hasRole('COORDINATOR') or hasRole('ADMIN')")
    public Map<String, Object> generateImpactReport(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = new HashMap<>();

        // Total de voluntarios activos
        long totalVolunteers = volunteerRepository.count();
        report.put("totalVolunteers", totalVolunteers);

        // Total de actividades realizadas en el período
        List<Activity> activitiesInPeriod = activityRepository.findByStartDateBetween(startDate, endDate);
        report.put("totalActivities", activitiesInPeriod.size());

        // Total de asignaciones en el período
        List<ActivityAssignment> assignmentsInPeriod = activityAssignmentRepository
                .findByActivityStartDateBetween(startDate, endDate);
        report.put("totalAssignments", assignmentsInPeriod.size());

        // Total de asistencias confirmadas en el período
        List<Attendance> attendancesInPeriod = attendanceRepository
                .findByActivityStartDateBetweenAndStatus(startDate, endDate, "CONFIRMED");
        report.put("totalAttendances", attendancesInPeriod.size());

        // Horas totales de voluntariado en el período
        int totalHours = attendancesInPeriod.stream()
                .mapToInt(attendance -> attendance.getActivity().getDurationHours())
                .sum();
        report.put("totalVolunteerHours", totalHours);

        // Promedio de horas por voluntario
        double averageHoursPerVolunteer = totalVolunteers > 0 ? (double) totalHours / totalVolunteers : 0.0;
        report.put("averageHoursPerVolunteer", averageHoursPerVolunteer);

        // Tasa de asistencia (asistencias / asignaciones)
        double attendanceRate = assignmentsInPeriod.size() > 0 ?
                (double) attendancesInPeriod.size() / assignmentsInPeriod.size() * 100 : 0.0;
        report.put("attendanceRate", attendanceRate);

        // Actividades por tipo
        Map<String, Long> activitiesByType = new HashMap<>();
        activitiesInPeriod.forEach(activity -> {
            String type = activity.getType() != null ? activity.getType() : "Sin tipo";
            activitiesByType.put(type, activitiesByType.getOrDefault(type, 0L) + 1);
        });
        report.put("activitiesByType", activitiesByType);

        // Metadatos del reporte
        report.put("reportPeriod", Map.of(
                "startDate", startDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                "endDate", endDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
        ));
        report.put("generatedAt", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        return report;
    }

    @PreAuthorize("hasRole('COORDINATOR') or hasRole('ADMIN')")
    public Map<String, Object> generateVolunteerImpactReport(Long volunteerId) {
        Map<String, Object> report = new HashMap<>();

        Volunteer volunteer = volunteerRepository.findById(volunteerId)
                .orElseThrow(() -> new RuntimeException("Volunteer not found"));

        // Información básica del voluntario
        report.put("volunteerId", volunteer.getId());
        report.put("volunteerName", volunteer.getFirstName() + " " + volunteer.getLastName());

        // Total de actividades asignadas
        List<ActivityAssignment> assignments = activityAssignmentRepository.findByVolunteerId(volunteerId);
        report.put("totalAssignedActivities", assignments.size());

        // Total de asistencias confirmadas
        List<Attendance> attendances = attendanceRepository.findByVolunteerIdAndStatus(volunteerId, "CONFIRMED");
        report.put("totalConfirmedAttendances", attendances.size());

        // Horas totales de voluntariado
        int totalHours = attendances.stream()
                .mapToInt(attendance -> attendance.getActivity().getDurationHours())
                .sum();
        report.put("totalVolunteerHours", totalHours);

        // Tasa de asistencia personal
        double personalAttendanceRate = assignments.size() > 0 ?
                (double) attendances.size() / assignments.size() * 100 : 0.0;
        report.put("personalAttendanceRate", personalAttendanceRate);

        // Actividades por tipo
        Map<String, Integer> activitiesByType = new HashMap<>();
        attendances.forEach(attendance -> {
            String type = attendance.getActivity().getType() != null ? attendance.getActivity().getType() : "Sin tipo";
            activitiesByType.put(type, activitiesByType.getOrDefault(type, 0) + 1);
        });
        report.put("activitiesByType", activitiesByType);

        // Metadatos del reporte
        report.put("generatedAt", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        return report;
    }
}