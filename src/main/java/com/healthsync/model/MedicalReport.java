package com.healthsync.model;
import com.healthsync.visitor.HealthcareEntityVisitor;
/** Generated medical report metadata usable by visitors. */
public record MedicalReport(String id, String patientId, String title) implements VisitableMedicalEntity { public void accept(HealthcareEntityVisitor visitor) { visitor.visitMedicalReport(this); } }
