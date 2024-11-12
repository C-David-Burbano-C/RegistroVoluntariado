package com.ong.registrovoluntariado.repository;

import com.ong.registrovoluntariado.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    List<Evaluation> findByVolunteerId(Long volunteerId);

    List<Evaluation> findByActivityId(Long activityId);

    List<Evaluation> findByEvaluatorId(Long evaluatorId);
}