package com.healthsync.abstractfactory;

import com.healthsync.model.Appointment;
import java.time.Instant;

/** Abstract appointment product; environments supply their own workflow defaults. */
public interface HealthcareAppointment {
    Appointment create(String id, String patientId, String doctorId, Instant scheduledAt);
    String environmentName();
}
