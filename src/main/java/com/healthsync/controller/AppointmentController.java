package com.healthsync.controller;

import com.healthsync.model.Appointment;
import com.healthsync.model.AppointmentStatus;
import com.healthsync.service.AppointmentService;
import com.healthsync.service.AppointmentBookingRequest;
import com.healthsync.strategy.AppointmentPriorityStrategy;
import com.healthsync.strategy.EmergencyPriority;
import com.healthsync.strategy.FollowUpPriority;
import com.healthsync.strategy.NormalPriority;

import java.time.Instant;

/** Coordinates scheduling UI actions. */
public class AppointmentController {
    private final AppointmentService service;
    public AppointmentController(AppointmentService service) { this.service = service; }

    public Appointment book(String patientId, String doctorId, Instant scheduledAt, AppointmentPriorityStrategy strategy) {
        return service.book(new AppointmentBookingRequest(patientId, doctorId, scheduledAt, strategy));
    }
    public Appointment cancel(String id) { return service.cancel(id); }
    public Appointment confirm(String id) { return service.confirm(id); }
    public Appointment complete(String id) { return service.complete(id); }
    public Appointment checkIn(String id) { return service.checkIn(id); }
    public Appointment startConsultation(String id) { return service.startConsultation(id); }
    public Appointment reject(String id) { return service.reject(id); }

    public static AppointmentPriorityStrategy priorityFor(String name) {
        return switch (name == null ? "NORMAL" : name.toUpperCase()) {
            case "EMERGENCY" -> new EmergencyPriority();
            case "FOLLOW_UP" -> new FollowUpPriority();
            default -> new NormalPriority();
        };
    }
}