package com.healthsync.model;

/** Patient-specific account and care identity. */
public final class Patient extends User implements VisitableMedicalEntity {
    private String patientNumber;
    public Patient(String id, String fullName, String email, String passwordHash, String patientNumber) {
        super(id, fullName, email, passwordHash, Role.PATIENT); this.patientNumber = patientNumber;
    }
    public String getPatientNumber() { return patientNumber; }
    public void accept(com.healthsync.visitor.HealthcareEntityVisitor visitor) { visitor.visitPatient(this); }
    // TODO: Add demographics, emergency contacts, allergies, and insurance profile.
}
