package com.healthsync.model;

/** Clinician account with professional credentials. */
public final class Doctor extends User {
    private String licenseNumber;
    private String specialtyCode;
    public Doctor(String id, String fullName, String email, String passwordHash, String licenseNumber, String specialtyCode) {
        super(id, fullName, email, passwordHash, Role.DOCTOR); this.licenseNumber = licenseNumber; this.specialtyCode = specialtyCode;
    }
    public String getLicenseNumber() { return licenseNumber; }
    public String getSpecialtyCode() { return specialtyCode; }
    // TODO: Add availability and assigned-patient information.
}
