package com.ong.registrovoluntariado.repository;

import com.ong.registrovoluntariado.entity.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {

    Optional<Volunteer> findByEmail(String email);

    Optional<Volunteer> findByIdentityDocument(String identityDocument);

    boolean existsByEmail(String email);

    boolean existsByIdentityDocument(String identityDocument);
}