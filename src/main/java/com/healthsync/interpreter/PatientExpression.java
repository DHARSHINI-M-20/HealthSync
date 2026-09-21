package com.healthsync.interpreter;

import com.healthsync.model.Appointment;

/** Matches an appointment by its patient identifier. */
public class PatientExpression implements ReportExpression {
    private final String patientId;
    public PatientExpression(String patientId) { this.patientId = patientId; }
    @Override public boolean interpret(Appointment appointment) { return patientId.equals(appointment.getPatientId()); }
}