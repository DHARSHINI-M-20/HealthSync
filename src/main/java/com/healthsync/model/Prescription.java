package com.healthsync.model;

import java.time.Instant;
import java.util.List;

/** Medication directions issued during a consultation. */
public class Prescription implements VisitableMedicalEntity {
    private final String id; private final String patientId; private final String doctorId; private final Instant issuedAt; private final List<PrescriptionItem> items;
    public Prescription(String id, String patientId, String doctorId, Instant issuedAt, List<PrescriptionItem> items) {
        this.id = id; this.patientId = patientId; this.doctorId = doctorId; this.issuedAt = issuedAt; this.items = List.copyOf(items);
    }
    public String getId() { return id; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public Instant getIssuedAt() { return issuedAt; }
    public List<PrescriptionItem> getItems() { return items; }
    public void accept(com.healthsync.visitor.HealthcareEntityVisitor visitor) { visitor.visitPrescription(this); }
    // TODO: Add refill and expiry handling.
}
