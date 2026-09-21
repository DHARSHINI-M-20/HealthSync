package com.healthsync.factory;
import com.healthsync.model.Patient;
import com.healthsync.model.Role;
import com.healthsync.model.User;
/** Factory Method implementation that creates Patient accounts. */
public final class PatientFactory extends UserFactory {
    public Role supportedRole() { return Role.PATIENT; }
    protected User createUser(UserCreationData data) { return new Patient(data.id(), data.fullName(), data.email(), data.passwordHash(), data.patientNumber()); }
}
