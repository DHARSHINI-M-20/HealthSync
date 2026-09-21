package com.healthsync.prototype;

import com.healthsync.builder.MedicalRecordBuilder;
import com.healthsync.model.RecordType;
import java.util.ArrayList;
import java.util.List;

/** Cloneable prototype carrying reusable consultation defaults, never a persisted patient record. */
public abstract class MedicalRecordTemplate implements Cloneable {
    private String diagnosis = "";
    private List<String> symptoms = new ArrayList<>();
    private String prescription = "";
    private List<String> testResults = new ArrayList<>();
    private String notes = "";
    private RecordType recordType;

    protected MedicalRecordTemplate(RecordType recordType) { this.recordType=recordType; }
    public MedicalRecordTemplate withDiagnosis(String diagnosis) { this.diagnosis=diagnosis; return this; }
    public MedicalRecordTemplate withSymptoms(List<String> symptoms) { this.symptoms=new ArrayList<>(symptoms); return this; }
    public MedicalRecordTemplate withPrescription(String prescription) { this.prescription=prescription; return this; }
    public MedicalRecordTemplate withTestResults(List<String> testResults) { this.testResults=new ArrayList<>(testResults); return this; }
    public MedicalRecordTemplate withNotes(String notes) { this.notes=notes; return this; }

    /** Returns an independent clone, including independent mutable lists. */
    public MedicalRecordTemplate copy() {
        try {
            MedicalRecordTemplate clone=(MedicalRecordTemplate) super.clone();
            clone.symptoms=new ArrayList<>(symptoms); clone.testResults=new ArrayList<>(testResults);
            return clone;
        } catch (CloneNotSupportedException exception) { throw new AssertionError(exception); }
    }

    /** Applies template defaults to a builder for an actual patient record. */
    public MedicalRecordBuilder applyTo(MedicalRecordBuilder builder) {
        return builder.withDiagnosis(diagnosis).withSymptoms(symptoms).withPrescription(prescription)
                .withTestResults(testResults).withNotes(notes).withRecordType(recordType);
    }
}
