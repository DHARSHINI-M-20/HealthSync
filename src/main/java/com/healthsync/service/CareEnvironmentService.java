package com.healthsync.service;

import com.healthsync.abstractfactory.HealthcareFactory;
import com.healthsync.model.Appointment;
import com.healthsync.model.Notification;
import com.healthsync.model.Prescription;
import com.healthsync.model.PrescriptionItem;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/** Client of the Abstract Factory; it is independent of Hospital and Clinic concrete product classes. */
public class CareEnvironmentService {
    private final HealthcareFactory healthcareFactory;

    public CareEnvironmentService(HealthcareFactory healthcareFactory) { this.healthcareFactory = healthcareFactory; }

    public Appointment createAppointment(String patientId, String doctorId, Instant scheduledAt) {
        return healthcareFactory.createAppointmentService().create(UUID.randomUUID().toString(), patientId, doctorId, scheduledAt);
    }

    public Prescription createPrescription(String patientId, String doctorId, List<PrescriptionItem> items) {
        return healthcareFactory.createPrescriptionService().create(UUID.randomUUID().toString(), patientId, doctorId, Instant.now(), items);
    }

    public Notification createNotification(String recipientId, String message) {
        return healthcareFactory.createNotificationService().create(UUID.randomUUID().toString(), recipientId, message, Instant.now());
    }
}
