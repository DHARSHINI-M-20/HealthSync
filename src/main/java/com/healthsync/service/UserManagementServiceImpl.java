package com.healthsync.service;

import com.healthsync.factory.UserCreationData;
import com.healthsync.factory.UserFactory;
import com.healthsync.model.*;
import com.healthsync.repository.*;
import com.healthsync.util.PasswordHasher;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Validates and registers users. This class owns business rules; repositories own MongoDB access.
 * It invokes UserFactory#create, so neither Main nor a Swing view instantiates concrete user types.
 */
public class UserManagementServiceImpl implements UserManagementService {
    private final Map<Role, UserFactory> factories = new EnumMap<>(Role.class);
    private final PatientRepository patients;
    private final DoctorRepository doctors;
    private final NurseRepository nurses;
    private final AdminRepository admins;

    public UserManagementServiceImpl(Collection<UserFactory> factories, PatientRepository patients,
                                     DoctorRepository doctors, NurseRepository nurses, AdminRepository admins) {
        for (UserFactory factory : factories) this.factories.put(factory.supportedRole(), factory);
        this.patients = patients; this.doctors = doctors; this.nurses = nurses; this.admins = admins;
    }

    @Override public User register(UserRegistrationRequest request) {
        validate(request);
        if (emailExists(request.email())) throw new IllegalArgumentException("An account already uses this email address");
        UserFactory factory = Optional.ofNullable(factories.get(request.role()))
                .orElseThrow(() -> new IllegalStateException("No user factory configured for " + request.role()));
        UserCreationData data = new UserCreationData(UUID.randomUUID().toString(), request.fullName().trim(),
                request.email().trim().toLowerCase(), PasswordHasher.hash(request.password()), request.role(),
                request.patientNumber(), request.licenseNumber(), request.specialtyCode(), request.nurseNumber(), request.department());
        User user = factory.create(data); // Factory Method dispatches to the role-specific creator.
        return save(user);
    }

    @Override public Optional<User> findById(Role role, String id) {
        return switch (role) { case PATIENT -> patients.findById(id).map(user -> user); case DOCTOR -> doctors.findById(id).map(user -> user); case NURSE -> nurses.findById(id).map(user -> user); case ADMIN -> admins.findById(id).map(user -> user); };
    }

    @Override public Optional<User> findByEmail(Role role, String email) {
        String normalized = email == null ? "" : email.trim().toLowerCase();
        return switch (role) { case PATIENT -> patients.findByEmail(normalized).map(user -> user); case DOCTOR -> doctors.findByEmail(normalized).map(user -> user); case NURSE -> nurses.findByEmail(normalized).map(user -> user); case ADMIN -> admins.findByEmail(normalized).map(user -> user); };
    }

    private User save(User user) {
        if (user instanceof Patient patient) return patients.save(patient);
        if (user instanceof Doctor doctor) return doctors.save(doctor);
        if (user instanceof Nurse nurse) return nurses.save(nurse);
        if (user instanceof Admin admin) return admins.save(admin);
        throw new IllegalArgumentException("Unsupported user type: " + user.getClass().getName());
    }

    private boolean emailExists(String email) {
        String normalized = email.trim().toLowerCase();
        return patients.findByEmail(normalized).isPresent() || doctors.findByEmail(normalized).isPresent()
                || nurses.findByEmail(normalized).isPresent() || admins.findByEmail(normalized).isPresent();
    }

    private void validate(UserRegistrationRequest request) {
        if (request == null) throw new IllegalArgumentException("Registration request is required");
        require(request.fullName(), "Full name"); require(request.email(), "Email"); require(request.password(), "Password");
        if (!request.email().contains("@")) throw new IllegalArgumentException("Email must be valid");
        if (request.password().length() < 8) throw new IllegalArgumentException("Password must contain at least 8 characters");
        if (request.role() == null) throw new IllegalArgumentException("Role is required");
        switch (request.role()) {
            case PATIENT -> require(request.patientNumber(), "Patient number");
            case DOCTOR -> { require(request.licenseNumber(), "License number"); require(request.specialtyCode(), "Specialty"); }
            case NURSE -> { require(request.nurseNumber(), "Nurse number"); require(request.department(), "Department"); }
            case ADMIN -> { /* Base fields are sufficient for admins. */ }
        }
    }

    private void require(String value, String field) { if (value == null || value.isBlank()) throw new IllegalArgumentException(field + " is required"); }
}
