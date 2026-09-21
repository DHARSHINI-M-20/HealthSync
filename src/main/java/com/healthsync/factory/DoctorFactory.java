package com.healthsync.factory;
import com.healthsync.model.Doctor;
import com.healthsync.model.Role;
import com.healthsync.model.User;
/** Factory Method implementation that creates Doctor accounts. */
public final class DoctorFactory extends UserFactory {
    public Role supportedRole() { return Role.DOCTOR; }
    protected User createUser(UserCreationData data) { return new Doctor(data.id(), data.fullName(), data.email(), data.passwordHash(), data.licenseNumber(), data.specialtyCode()); }
}
