package com.healthsync.abstractfactory;

import com.healthsync.model.Prescription;
import com.healthsync.model.PrescriptionItem;
import java.time.Instant;
import java.util.List;

/** Abstract prescription product; environments can apply different issuing policy later. */
public interface HealthcarePrescription {
    Prescription create(String id, String patientId, String doctorId, Instant issuedAt, List<PrescriptionItem> items);
    String environmentName();
}
