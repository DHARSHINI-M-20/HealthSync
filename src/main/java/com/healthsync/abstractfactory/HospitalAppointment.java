package com.healthsync.abstractfactory;

import com.healthsync.model.Appointment;
import com.healthsync.state.RequestedState;
import java.time.Instant;

/** Hospital appointment product; later supports facility and emergency-resource workflows. */
public final class HospitalAppointment implements HealthcareAppointment {
    public Appointment create(String id, String patientId, String doctorId, Instant scheduledAt) { return new Appointment(id, patientId, doctorId, scheduledAt, new RequestedState()); }
    public String environmentName() { return "HOSPITAL"; }
}
