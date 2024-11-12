package com.ong.registrovoluntariado.repository;

import com.ong.registrovoluntariado.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    List<Activity> findByActiveTrue();

    List<Activity> findByType(String type);

    List<Activity> findByStartDateBetween(LocalDate startDate, LocalDate endDate);

    boolean existsByName(String name);
}