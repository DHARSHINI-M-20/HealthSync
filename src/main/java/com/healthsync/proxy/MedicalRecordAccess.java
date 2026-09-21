package com.healthsync.proxy;
import com.healthsync.model.MedicalRecord;
import java.util.Optional;
public interface MedicalRecordAccess { Optional<MedicalRecord> findById(String recordId); }
