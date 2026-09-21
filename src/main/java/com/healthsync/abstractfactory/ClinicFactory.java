package com.healthsync.abstractfactory;

/** Produces the clinic-compatible appointment, prescription, and notification family. */
public final class ClinicFactory implements HealthcareFactory {
    public HealthcareAppointment createAppointmentService() { return new ClinicAppointment(); }
    public HealthcarePrescription createPrescriptionService() { return new ClinicPrescription(); }
    public HealthcareNotification createNotificationService() { return new ClinicNotification(); }
}
