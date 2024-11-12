package com.ong.registrovoluntariado.service;

import com.ong.registrovoluntariado.entity.Activity;
import com.ong.registrovoluntariado.entity.Certificate;
import com.ong.registrovoluntariado.entity.User;
import com.ong.registrovoluntariado.entity.Volunteer;
import com.ong.registrovoluntariado.repository.CertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private VolunteerService volunteerService;

    @Autowired
    private ActivityService activityService;

    @Transactional
    public Certificate generateCertificate(Long volunteerId, Long activityId, Double hoursWorked, User issuedBy) {
        Optional<Volunteer> volunteerOpt = volunteerService.getVolunteerById(volunteerId);
        Optional<Activity> activityOpt = activityService.getActivityById(activityId);

        if (volunteerOpt.isEmpty() || activityOpt.isEmpty()) {
            throw new RuntimeException("Volunteer or Activity not found");
        }

        Certificate certificate = new Certificate();
        certificate.setVolunteer(volunteerOpt.get());
        certificate.setActivity(activityOpt.get());
        certificate.setHoursWorked(hoursWorked);
        certificate.setIssuedBy(issuedBy);

        return certificateRepository.save(certificate);
    }

    public List<Certificate> getCertificatesByVolunteer(Long volunteerId) {
        return certificateRepository.findByVolunteerId(volunteerId);
    }

    public Optional<Certificate> getCertificateByCode(String code) {
        return certificateRepository.findByCertificateCode(code);
    }
}