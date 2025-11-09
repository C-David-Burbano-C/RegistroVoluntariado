package com.ong.registrovoluntariado.service;

import com.ong.registrovoluntariado.entity.Activity;
import com.ong.registrovoluntariado.entity.User;
import com.ong.registrovoluntariado.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Transactional
    public Activity createActivity(Activity activity, User createdBy) {
        if (activityRepository.existsByName(activity.getName())) {
            throw new RuntimeException("Activity name already exists");
        }
        activity.setCreatedBy(createdBy);
        return activityRepository.save(activity);
    }

    public List<Activity> getAllActiveActivities() {
        return activityRepository.findByActiveTrue();
    }

    public List<Activity> getActivitiesByType(String type) {
        return activityRepository.findByType(type);
    }

    public Optional<Activity> getActivityById(Long id) {
        return activityRepository.findById(id);
    }

    @Transactional
    public Activity updateActivity(Long id, Activity updatedActivity) {
        Optional<Activity> existingOpt = activityRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new RuntimeException("Activity not found");
        }
        Activity existing = existingOpt.get();

        if (!existing.getName().equals(updatedActivity.getName()) &&
            activityRepository.existsByName(updatedActivity.getName())) {
            throw new RuntimeException("Activity name already exists");
        }

        existing.setName(updatedActivity.getName());
        existing.setDescription(updatedActivity.getDescription());
        existing.setType(updatedActivity.getType());
        existing.setLocation(updatedActivity.getLocation());
        existing.setSchedule(updatedActivity.getSchedule());
        existing.setRequiredVolunteers(updatedActivity.getRequiredVolunteers());
        existing.setActive(updatedActivity.isActive());

        return activityRepository.save(existing);
    }

    @Transactional
    public void deactivateActivity(Long id) {
        Optional<Activity> activityOpt = activityRepository.findById(id);
        if (activityOpt.isPresent()) {
            Activity activity = activityOpt.get();
            activity.setActive(false);
            activityRepository.save(activity);
        }
    }
}