package com.healthsync.abstractfactory;

import com.healthsync.model.Appointment;
import com.healthsync.state.RequestedState;
import java.time.Instant;

/** Clinic appointment product; later supports outpatient slot and walk-in rules. */
public final class ClinicAppointment implements HealthcareAppointment {
    public Appointment create(String id, String patientId, String doctorId, Instant scheduledAt) { return new Appointment(id, patientId, doctorId, scheduledAt, new RequestedState()); }
    public String environmentName() { return "CLINIC"; }
}
