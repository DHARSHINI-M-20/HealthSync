package com.healthsync.service;
import com.healthsync.model.MedicalRecord;
import java.util.List;
public interface MedicalRecordService { MedicalRecord save(MedicalRecord record); List<MedicalRecord> getPatientHistory(String patientId); List<MedicalRecord> findAll(); }
