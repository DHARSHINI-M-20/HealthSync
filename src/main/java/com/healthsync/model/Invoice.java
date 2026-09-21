package com.healthsync.model;
import java.math.BigDecimal;
/** Persisted billing snapshot for an appointment. */
public class Invoice {
    private final String id; private final String patientId; private final String appointmentId; private final BigDecimal total;
    public Invoice(String id, String patientId, String appointmentId, BigDecimal total) { this.id = id; this.patientId = patientId; this.appointmentId = appointmentId; this.total = total; }
    public String getId() { return id; } public String getPatientId() { return patientId; } public String getAppointmentId() { return appointmentId; } public BigDecimal getTotal() { return total; }
    // TODO: Add line items, payment status, and insurance details.
}
