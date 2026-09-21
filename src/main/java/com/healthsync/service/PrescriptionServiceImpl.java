package com.healthsync.service;

import com.healthsync.model.Prescription;
import com.healthsync.repository.PrescriptionRepository;
import com.healthsync.model.Role;
import com.healthsync.util.AuthorizationGuard;
import java.util.List;
import java.util.Optional;

/** Default repository-backed prescription service. */
public class PrescriptionServiceImpl implements PrescriptionService {
    private final PrescriptionRepository repository;
    public PrescriptionServiceImpl(PrescriptionRepository repository) { this.repository = repository; }
    @Override public Prescription save(Prescription prescription) {
        AuthorizationGuard.requireAny(Role.DOCTOR, Role.ADMIN);
        return repository.save(prescription);
    }
    @Override public Optional<Prescription> findById(String id) { return repository.findById(id); }
    @Override public List<Prescription> findByPatientId(String patientId) { return repository.findByPatientId(patientId); }
    @Override public List<Prescription> findAll() { return repository.findAll(); }
}