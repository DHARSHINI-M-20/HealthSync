package com.healthsync.service;

import com.healthsync.model.MedicalRecord;
import com.healthsync.repository.MedicalRecordRepository;
import java.util.List;

/** Default repository-backed medical-record service. */
public class MedicalRecordServiceImpl implements MedicalRecordService {
    private final MedicalRecordRepository repository;
    public MedicalRecordServiceImpl(MedicalRecordRepository repository) { this.repository = repository; }
    @Override public MedicalRecord save(MedicalRecord record) { return repository.save(record); }
    @Override public List<MedicalRecord> getPatientHistory(String patientId) { return repository.findByPatientId(patientId); }
    @Override public List<MedicalRecord> findAll() { return repository.findAll(); }
}