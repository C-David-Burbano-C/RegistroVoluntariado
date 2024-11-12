package com.ong.registrovoluntariado.repository;

import com.ong.registrovoluntariado.entity.ActivityAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ActivityAssignmentRepository extends JpaRepository<ActivityAssignment, Long> {

    List<ActivityAssignment> findByVolunteerId(Long volunteerId);

    List<ActivityAssignment> findByActivityId(Long activityId);

    List<ActivityAssignment> findByStatus(String status);

    List<ActivityAssignment> findByStatusNot(String status);

    @Query("SELECT aa FROM ActivityAssignment aa WHERE aa.activity.startDate BETWEEN :startDate AND :endDate")
    List<ActivityAssignment> findByActivityStartDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}