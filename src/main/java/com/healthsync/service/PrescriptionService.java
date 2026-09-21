package com.healthsync.service;

import com.healthsync.model.Prescription;
import java.util.List;
import java.util.Optional;

/** Application boundary for prescription use cases. */
public interface PrescriptionService {
    Prescription save(Prescription prescription);
    Optional<Prescription> findById(String id);
    List<Prescription> findByPatientId(String patientId);
    List<Prescription> findAll();
}
