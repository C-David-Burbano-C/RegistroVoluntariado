package com.ong.registrovoluntariado.service;

import com.ong.registrovoluntariado.entity.User;
import com.ong.registrovoluntariado.entity.Volunteer;
import com.ong.registrovoluntariado.repository.VolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VolunteerService {

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public Volunteer registerVolunteer(Volunteer volunteer) {
        if (volunteerRepository.existsByEmail(volunteer.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (volunteerRepository.existsByIdentityDocument(volunteer.getIdentityDocument())) {
            throw new RuntimeException("Identity document already exists");
        }

        // Create user account
        User user = new User();
        user.setUsername(volunteer.getEmail());
        user.setEmail(volunteer.getEmail());
        user.setPassword("defaultPassword123!"); // Should be changed later
        user.setRole(User.Role.VOLUNTEER);
        user = userService.createUser(user);

        volunteer.setUser(user);
        return volunteerRepository.save(volunteer);
    }

    public List<Volunteer> getAllVolunteers() {
        return volunteerRepository.findAll();
    }

    public Optional<Volunteer> getVolunteerById(Long id) {
        return volunteerRepository.findById(id);
    }

    public Optional<Volunteer> getVolunteerByEmail(String email) {
        return volunteerRepository.findByEmail(email);
    }

    @Transactional
    public Volunteer updateVolunteer(Long id, Volunteer updatedVolunteer) {
        Optional<Volunteer> existingOpt = volunteerRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("Volunteer not found");
        }
        Volunteer existing = existingOpt.get();

        // Check for unique constraints
        if (!existing.getEmail().equals(updatedVolunteer.getEmail()) &&
            volunteerRepository.existsByEmail(updatedVolunteer.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (!existing.getIdentityDocument().equals(updatedVolunteer.getIdentityDocument()) &&
            volunteerRepository.existsByIdentityDocument(updatedVolunteer.getIdentityDocument())) {
            throw new RuntimeException("Identity document already exists");
        }

        existing.setFullName(updatedVolunteer.getFullName());
        existing.setEmail(updatedVolunteer.getEmail());
        existing.setPhone(updatedVolunteer.getPhone());
        existing.setBirthDate(updatedVolunteer.getBirthDate());
        existing.setAddress(updatedVolunteer.getAddress());
        existing.setIdentityDocument(updatedVolunteer.getIdentityDocument());

        return volunteerRepository.save(existing);
    }
}