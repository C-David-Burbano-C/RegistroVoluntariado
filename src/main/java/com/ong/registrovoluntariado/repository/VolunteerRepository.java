package com.ong.registrovoluntariado.repository;

import com.ong.registrovoluntariado.entity.Volunteer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long> {

    Optional<Volunteer> findByEmail(String email);

    Optional<Volunteer> findByIdentityDocument(String identityDocument);

    boolean existsByEmail(String email);

    boolean existsByIdentityDocument(String identityDocument);

    @Query("SELECT v FROM Volunteer v WHERE " +
           "LOWER(v.fullName) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(v.email) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "v.phone LIKE CONCAT('%', :query, '%') OR " +
           "v.identityDocument LIKE CONCAT('%', :query, '%')")
    List<Volunteer> searchVolunteers(@Param("query") String query);
}