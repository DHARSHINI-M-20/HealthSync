package com.healthsync.repository;
import com.healthsync.model.Patient;
import java.util.Optional;
/** Persistence boundary for patient profiles. */
public interface PatientRepository extends Repository<Patient> { Optional<Patient> findByEmail(String email); }
