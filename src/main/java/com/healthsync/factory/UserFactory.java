package com.healthsync.factory;

import com.healthsync.model.Role;
import com.healthsync.model.User;

/** Factory Method creator. Subclasses choose the concrete User type while callers depend only on User. */
public abstract class UserFactory {
    public final User create(UserCreationData data) {
        if (data.role() != supportedRole()) throw new IllegalArgumentException("Factory does not support role: " + data.role());
        return createUser(data); // Factory Method invocation
    }
    public abstract Role supportedRole();
    protected abstract User createUser(UserCreationData data); // Factory Method
}
