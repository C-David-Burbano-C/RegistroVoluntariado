package com.ong.registrovoluntariado.repository;

import com.ong.registrovoluntariado.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByAssignmentId(Long assignmentId);

    Optional<Attendance> findByAssignmentIdAndDate(Long assignmentId, LocalDate date);

    List<Attendance> findByDate(LocalDate date);
}