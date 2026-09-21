package com.healthsync.abstractfactory;

/** Abstract Factory for a compatible family of healthcare service products. */
public interface HealthcareFactory {
    HealthcareAppointment createAppointmentService();
    HealthcarePrescription createPrescriptionService();
    HealthcareNotification createNotificationService();
}
