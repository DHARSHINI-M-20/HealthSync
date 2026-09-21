package com.healthsync.composite;
import com.healthsync.visitor.MedicalRecordVisitor;
/** Component in a hierarchical medical record. */
public interface MedicalRecordComponent { String summary(); void accept(MedicalRecordVisitor visitor); }
