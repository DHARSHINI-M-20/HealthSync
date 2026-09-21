package com.healthsync.factory;
import com.healthsync.model.Nurse;
import com.healthsync.model.Role;
import com.healthsync.model.User;
/** Factory Method implementation that creates Nurse accounts. */
public final class NurseFactory extends UserFactory {
    public Role supportedRole() { return Role.NURSE; }
    protected User createUser(UserCreationData data) { return new Nurse(data.id(), data.fullName(), data.email(), data.passwordHash(), data.nurseNumber(), data.department()); }
}
