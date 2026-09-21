package com.healthsync.visitor;

import com.healthsync.model.Diagnosis;
import com.healthsync.model.MedicalReport;
import com.healthsync.model.Patient;
import com.healthsync.model.Prescription;

/** Visitor contract for the concrete patient-care entities that implement VisitableMedicalEntity. */
public interface HealthcareEntityVisitor {
    void visitPatient(Patient patient);
    void visitPrescription(Prescription prescription);
    void visitDiagnosis(Diagnosis diagnosis);
    void visitMedicalReport(MedicalReport report);
}