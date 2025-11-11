package com.ong.registrovoluntariado.service;

import com.ong.registrovoluntariado.entity.ActivityAssignment;
import com.ong.registrovoluntariado.entity.Attendance;
import com.ong.registrovoluntariado.entity.User;
import com.ong.registrovoluntariado.repository.ActivityAssignmentRepository;
import com.ong.registrovoluntariado.repository.AttendanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private ActivityAssignmentRepository assignmentRepository;

    @Transactional
    public Attendance recordAttendance(Long assignmentId, LocalDate date, Attendance.Status status,
                                     LocalTime checkInTime, LocalTime checkOutTime,
                                     String observations, Attendance.PerformanceLevel performanceLevel,
                                     User recordedBy) {
        Optional<ActivityAssignment> assignmentOpt = assignmentRepository.findById(assignmentId);
        if (assignmentOpt.isEmpty()) {
            throw new RuntimeException("Assignment not found");
        }

        ActivityAssignment assignment = assignmentOpt.get();

        // Check if attendance already exists for this date
        Optional<Attendance> existing = attendanceRepository.findByAssignmentIdAndDate(assignmentId, date);
        if (existing.isPresent()) {
            throw new RuntimeException("Attendance already recorded for this date");
        }

        Attendance attendance = new Attendance();
        attendance.setAssignment(assignment);
        attendance.setDate(date);
        attendance.setStatus(status);
        attendance.setCheckInTime(checkInTime);
        attendance.setCheckOutTime(checkOutTime);
        attendance.setObservations(observations);
        attendance.setPerformanceLevel(performanceLevel);
        attendance.setRecordedBy(recordedBy);

        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getAttendanceByAssignment(Long assignmentId) {
        return attendanceRepository.findByAssignmentId(assignmentId);
    }

    @Transactional
    public Attendance updateAttendance(Long id, Attendance updatedAttendance) {
        Optional<Attendance> existingOpt = attendanceRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("Attendance not found");
        }
        Attendance existing = existingOpt.get();

        existing.setStatus(updatedAttendance.getStatus());
        existing.setCheckInTime(updatedAttendance.getCheckInTime());
        existing.setCheckOutTime(updatedAttendance.getCheckOutTime());
        existing.setObservations(updatedAttendance.getObservations());
        existing.setPerformanceLevel(updatedAttendance.getPerformanceLevel());

        return attendanceRepository.save(existing);
    }
}