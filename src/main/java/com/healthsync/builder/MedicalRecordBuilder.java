package com.healthsync.builder;

import com.healthsync.model.MedicalRecord;
import com.healthsync.model.RecordType;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/** Builder for a complex, immutable clinical MedicalRecord. */
public class MedicalRecordBuilder {
    private String recordId, patientId, doctorId, diagnosis = "", prescription = "", notes = "";
    private List<String> symptoms = new ArrayList<>(), testResults = new ArrayList<>();
    private Instant date = Instant.now();
    private RecordType recordType;

    public MedicalRecordBuilder withRecordId(String recordId) { this.recordId=recordId; return this; }
    public MedicalRecordBuilder withPatientId(String patientId) { this.patientId=patientId; return this; }
    public MedicalRecordBuilder withDoctorId(String doctorId) { this.doctorId=doctorId; return this; }
    public MedicalRecordBuilder withDiagnosis(String diagnosis) { this.diagnosis=diagnosis; return this; }
    public MedicalRecordBuilder withSymptoms(List<String> symptoms) { this.symptoms=new ArrayList<>(symptoms); return this; }
    public MedicalRecordBuilder addSymptom(String symptom) { this.symptoms.add(symptom); return this; }
    public MedicalRecordBuilder withPrescription(String prescription) { this.prescription=prescription; return this; }
    public MedicalRecordBuilder withTestResults(List<String> testResults) { this.testResults=new ArrayList<>(testResults); return this; }
    public MedicalRecordBuilder addTestResult(String testResult) { this.testResults.add(testResult); return this; }
    public MedicalRecordBuilder withNotes(String notes) { this.notes=notes; return this; }
    public MedicalRecordBuilder withDate(Instant date) { this.date=date; return this; }
    public MedicalRecordBuilder withRecordType(RecordType recordType) { this.recordType=recordType; return this; }

    public MedicalRecord build() {
        require(recordId, "Record ID"); require(patientId, "Patient ID"); require(doctorId, "Doctor ID");
        if (date == null) throw new IllegalStateException("Record date is required");
        if (recordType == null) throw new IllegalStateException("Record type is required");
        return new MedicalRecord(recordId, patientId, doctorId, diagnosis, symptoms, prescription, testResults, notes, date, recordType);
    }
    private void require(String value, String name) { if (value == null || value.isBlank()) throw new IllegalStateException(name + " is required"); }
}
