package com.healthsync.repository;
import com.healthsync.model.Prescription;
import java.util.List;
/** Persistence boundary for prescriptions. */
public interface PrescriptionRepository extends Repository<Prescription> { List<Prescription> findByPatientId(String patientId); }
