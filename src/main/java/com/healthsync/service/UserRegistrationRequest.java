package com.healthsync.service;
import com.healthsync.model.Role;
/** Raw registration input accepted by the user-management service; password is never persisted as supplied. */
public record UserRegistrationRequest(String fullName, String email, String password, Role role,
                                      String patientNumber, String licenseNumber, String specialtyCode,
                                      String nurseNumber, String department) { }
