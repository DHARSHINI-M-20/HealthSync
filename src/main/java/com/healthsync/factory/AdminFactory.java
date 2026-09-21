package com.healthsync.factory;
import com.healthsync.model.Admin;
import com.healthsync.model.Role;
import com.healthsync.model.User;
/** Factory Method implementation that creates Admin accounts. */
public final class AdminFactory extends UserFactory {
    public Role supportedRole() { return Role.ADMIN; }
    protected User createUser(UserCreationData data) { return new Admin(data.id(), data.fullName(), data.email(), data.passwordHash()); }
}
