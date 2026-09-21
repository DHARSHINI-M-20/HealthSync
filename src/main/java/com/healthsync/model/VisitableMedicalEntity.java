package com.healthsync.model;
import com.healthsync.visitor.HealthcareEntityVisitor;
/** Visitor entry point for patient-care entities. */
public interface VisitableMedicalEntity { void accept(HealthcareEntityVisitor visitor); }
