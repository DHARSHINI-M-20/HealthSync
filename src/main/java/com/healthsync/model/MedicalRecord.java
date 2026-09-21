package com.healthsync.model;

import java.time.Instant;
import java.util.List;

/** Immutable clinical record created through MedicalRecordBuilder. */
public final class MedicalRecord {
    private final String recordId;
    private final String patientId;
    private final String doctorId;
    private final String diagnosis;
    private final List<String> symptoms;
    private final String prescription;
    private final List<String> testResults;
    private final String notes;
    private final Instant date;
    private final RecordType recordType;

    public MedicalRecord(String recordId, String patientId, String doctorId, String diagnosis, List<String> symptoms,
                         String prescription, List<String> testResults, String notes, Instant date, RecordType recordType) {
        this.recordId=recordId; this.patientId=patientId; this.doctorId=doctorId; this.diagnosis=diagnosis;
        this.symptoms=List.copyOf(symptoms); this.prescription=prescription; this.testResults=List.copyOf(testResults);
        this.notes=notes; this.date=date; this.recordType=recordType;
    }
    public String getId() { return recordId; }
    public String getRecordId() { return recordId; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public String getDiagnosis() { return diagnosis; }
    public List<String> getSymptoms() { return symptoms; }
    public String getPrescription() { return prescription; }
    public List<String> getTestResults() { return testResults; }
    public String getNotes() { return notes; }
    public Instant getDate() { return date; }
    public RecordType getRecordType() { return recordType; }
}
