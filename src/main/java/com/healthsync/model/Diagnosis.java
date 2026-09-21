package com.healthsync.model;
import com.healthsync.visitor.HealthcareEntityVisitor;
/** Structured diagnosis entity usable by reporting, billing, and audit visitors. */
public record Diagnosis(String code, String description) implements VisitableMedicalEntity { public void accept(HealthcareEntityVisitor visitor) { visitor.visitDiagnosis(this); } }
