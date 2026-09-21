package com.healthsync.service;
import com.healthsync.model.Patient;
import java.util.Optional;
public interface PatientService { Patient register(Patient patient); Optional<Patient> findPatient(String patientId); }
