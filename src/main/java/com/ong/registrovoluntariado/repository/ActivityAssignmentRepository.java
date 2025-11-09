package com.ong.registrovoluntariado.repository;

import com.ong.registrovoluntariado.entity.ActivityAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityAssignmentRepository extends JpaRepository<ActivityAssignment, Long> {

    List<ActivityAssignment> findByVolunteerId(Long volunteerId);

    List<ActivityAssignment> findByActivityId(Long activityId);

    List<ActivityAssignment> findByStatus(ActivityAssignment.Status status);
}