package com.ong.registrovoluntariado.service;

import com.ong.registrovoluntariado.entity.Activity;
import com.ong.registrovoluntariado.entity.Volunteer;
import com.ong.registrovoluntariado.repository.ActivityRepository;
import com.ong.registrovoluntariado.repository.VolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private ActivityRepository activityRepository;

    public List<Volunteer> searchVolunteers(String query) {
        return volunteerRepository.searchVolunteers(query);
    }

    public List<Activity> searchActivitiesByType(String type) {
        return activityRepository.findByType(type);
    }
}