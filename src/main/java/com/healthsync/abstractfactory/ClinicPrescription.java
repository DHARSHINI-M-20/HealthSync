package com.healthsync.abstractfactory;

import com.healthsync.model.Prescription;
import com.healthsync.model.PrescriptionItem;
import java.time.Instant;
import java.util.List;

/** Clinic prescription product; later supports outpatient dispensing/refill policies. */
public final class ClinicPrescription implements HealthcarePrescription {
    public Prescription create(String id, String patientId, String doctorId, Instant issuedAt, List<PrescriptionItem> items) { return new Prescription(id, patientId, doctorId, issuedAt, items); }
    public String environmentName() { return "CLINIC"; }
}
