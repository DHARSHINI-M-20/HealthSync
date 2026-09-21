package com.healthsync.repository;
import com.healthsync.model.MedicalRecord;
import java.util.List;
public interface MedicalRecordRepository extends Repository<MedicalRecord> { List<MedicalRecord> findByPatientId(String patientId); }
