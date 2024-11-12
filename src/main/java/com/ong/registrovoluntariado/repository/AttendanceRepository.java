package com.ong.registrovoluntariado.repository;

import com.ong.registrovoluntariado.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByAssignmentId(Long assignmentId);

    Optional<Attendance> findByAssignmentIdAndDate(Long assignmentId, LocalDate date);

    List<Attendance> findByDate(LocalDate date);

    List<Attendance> findByVolunteerIdAndStatus(Long volunteerId, String status);

    @Query("SELECT a FROM Attendance a WHERE a.assignment.activity.startDate BETWEEN :startDate AND :endDate AND a.status = :status")
    List<Attendance> findByActivityStartDateBetweenAndStatus(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("status") String status);
}