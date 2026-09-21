package com.healthsync.controller;

import com.healthsync.model.MedicalRecord;
import com.healthsync.service.MedicalRecordService;
import com.healthsync.model.Role;
import com.healthsync.util.AuthorizationGuard;
import java.util.List;

/** Coordinates clinical record UI actions. */
public class MedicalRecordController {
    private final MedicalRecordService service;
    public MedicalRecordController(MedicalRecordService service) { this.service = service; }

    public MedicalRecord save(MedicalRecord record) {
        AuthorizationGuard.requireAny(Role.DOCTOR, Role.ADMIN);
        return service.save(record);
    }
    public List<MedicalRecord> getPatientHistory(String patientId) { return service.getPatientHistory(patientId); }
}