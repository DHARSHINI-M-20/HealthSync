package com.healthsync.factory;
import com.healthsync.model.Role;
/** Validated, password-hashed data passed from the registration service to a user factory. */
public record UserCreationData(String id, String fullName, String email, String passwordHash, Role role, String patientNumber, String licenseNumber, String specialtyCode, String nurseNumber, String department) { }
