package com.ong.registrovoluntariado.service;

import com.ong.registrovoluntariado.entity.ActivityAssignment;
import com.ong.registrovoluntariado.entity.Notification;
import com.ong.registrovoluntariado.entity.User;
import com.ong.registrovoluntariado.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Transactional
    public void sendActivityAssignmentNotification(ActivityAssignment assignment) {
        User recipient = assignment.getVolunteer().getUser();
        String subject = "Nueva actividad asignada: " + assignment.getActivity().getName();
        String message = String.format(
            "Hola %s,\n\nTe han asignado a la actividad '%s'.\n" +
            "Descripción: %s\nFecha de inicio: %s\nFecha de fin: %s\n\n" +
            "Accede al sistema para más detalles.",
            assignment.getVolunteer().getFullName(),
            assignment.getActivity().getName(),
            assignment.getActivity().getDescription(),
            assignment.getStartDate(),
            assignment.getEndDate()
        );

        Notification notification = new Notification();
        notification.setRecipient(recipient);
        notification.setSubject(subject);
        notification.setMessage(message);
        notification.setType(Notification.Type.EMAIL);
        notification.setRelatedActivity(assignment.getActivity());

        notificationRepository.save(notification);

        sendEmail(notification);
    }

    @Async
    public void sendEmail(Notification notification) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(notification.getRecipient().getEmail());
            mailMessage.setSubject(notification.getSubject());
            mailMessage.setText(notification.getMessage());

            mailSender.send(mailMessage);

            notification.setSent(true);
            notification.setSentAt(LocalDateTime.now());
            notificationRepository.save(notification);
        } catch (Exception e) {
            // Log error, retry logic could be implemented
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    public List<Notification> getUnsentNotifications() {
        return notificationRepository.findBySentFalse();
    }

    @Transactional
    public void processUnsentNotifications() {
        List<Notification> unsent = getUnsentNotifications();
        for (Notification notification : unsent) {
            sendEmail(notification);
        }
    }
}