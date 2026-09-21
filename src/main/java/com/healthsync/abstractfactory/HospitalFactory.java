package com.healthsync.abstractfactory;

/** Produces the hospital-compatible appointment, prescription, and notification family. */
public final class HospitalFactory implements HealthcareFactory {
    public HealthcareAppointment createAppointmentService() { return new HospitalAppointment(); }
    public HealthcarePrescription createPrescriptionService() { return new HospitalPrescription(); }
    public HealthcareNotification createNotificationService() { return new HospitalNotification(); }
}
