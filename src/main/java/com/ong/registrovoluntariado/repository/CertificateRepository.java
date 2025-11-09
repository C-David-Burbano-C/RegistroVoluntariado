package com.ong.registrovoluntariado.repository;

import com.ong.registrovoluntariado.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    List<Certificate> findByVolunteerId(Long volunteerId);

    Optional<Certificate> findByCertificateCode(String certificateCode);
}