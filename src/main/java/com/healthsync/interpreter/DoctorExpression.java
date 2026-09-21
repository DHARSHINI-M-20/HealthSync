package com.healthsync.interpreter;

import com.healthsync.model.Appointment;

/** Matches an appointment by its assigned doctor identifier. */
public class DoctorExpression implements ReportExpression {
    private final String doctorId;
    public DoctorExpression(String doctorId) { this.doctorId = doctorId; }
    @Override
    public boolean interpret(Appointment appointment) { return doctorId.equals(appointment.getDoctorId()); }
}