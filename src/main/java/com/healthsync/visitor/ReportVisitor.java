package com.healthsync.visitor;

import com.healthsync.model.Diagnosis;
import com.healthsync.model.MedicalReport;
import com.healthsync.model.Patient;
import com.healthsync.model.Prescription;

import java.util.ArrayList;
import java.util.List;

/** Builds a human-readable clinical report from visited patient-care entities. */
public class ReportVisitor implements HealthcareEntityVisitor {

    private final List<String> sections = new ArrayList<>();

    @Override
    public void visitPatient(Patient patient) {
        sections.add("Patient: " + patient.getFullName() + " (#" + patient.getPatientNumber() + ")");
    }

    @Override
    public void visitPrescription(Prescription prescription) {
        StringBuilder line = new StringBuilder("Prescription #" + prescription.getId()
                + " issued by doctor #" + prescription.getDoctorId() + " on " + prescription.getIssuedAt());
        prescription.getItems().forEach(item -> line.append("\n  - ")
                .append(item.medication().name()).append(" ").append(item.dosage())
                .append(" (").append(item.instructions()).append(")"));
        sections.add(line.toString());
    }

    @Override
    public void visitDiagnosis(Diagnosis diagnosis) {
        sections.add("Diagnosis: " + diagnosis.code() + " - " + diagnosis.description());
    }

    @Override
    public void visitMedicalReport(MedicalReport report) {
        sections.add("Medical Report #" + report.id() + " (" + report.title() + ") for patient #" + report.patientId());
    }

    public List<String> getReport() {
        return List.copyOf(sections);
    }

    public String render() {
        return String.join("\n", sections);
    }
}