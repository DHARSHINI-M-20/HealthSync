package com.healthsync.controller;
import com.healthsync.model.Patient;
import com.healthsync.service.PatientService;
import java.util.Optional;
public class PatientController {
    private final PatientService service;
    public PatientController(PatientService service) { this.service = service; }
    public Patient register(Patient patient) { return service.register(patient); }
    public Optional<Patient> findPatient(String patientId) { return service.findPatient(patientId); }
}
