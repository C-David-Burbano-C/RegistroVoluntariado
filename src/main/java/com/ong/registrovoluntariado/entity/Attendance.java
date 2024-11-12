package com.ong.registrovoluntariado.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "attendances")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "assignment_id")
    private ActivityAssignment assignment;

    @NotNull
    private LocalDate date;

    private LocalTime checkInTime;

    private LocalTime checkOutTime;

    private String status;

    @Size(max = 500)
    private String observations;

    @Enumerated(EnumType.STRING)
    private PerformanceLevel performanceLevel;

    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "recorded_by")
    private User recordedBy;

    public enum Status {
        PRESENT, ABSENT
    }

    public enum PerformanceLevel {
        EXCELLENT, GOOD, REGULAR, DEFICIENT
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ActivityAssignment getAssignment() {
        return assignment;
    }

    public void setAssignment(ActivityAssignment assignment) {
        this.assignment = assignment;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public LocalTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(LocalTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public PerformanceLevel getPerformanceLevel() {
        return performanceLevel;
    }

    public void setPerformanceLevel(PerformanceLevel performanceLevel) {
        this.performanceLevel = performanceLevel;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public User getRecordedBy() {
        return recordedBy;
    }

    public void setRecordedBy(User recordedBy) {
        this.recordedBy = recordedBy;
    }

    public Activity getActivity() {
        return assignment != null ? assignment.getActivity() : null;
    }

    public Volunteer getVolunteer() {
        return assignment != null ? assignment.getVolunteer() : null;
    }
}