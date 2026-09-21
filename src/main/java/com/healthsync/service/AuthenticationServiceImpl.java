package com.healthsync.service;

import com.healthsync.model.User;
import com.healthsync.util.PasswordHasher;
import com.healthsync.util.SessionContext;
import java.util.Optional;

/** Verifies supplied credentials against persisted users and owns the login session. */
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserManagementService users;
    public AuthenticationServiceImpl(UserManagementService users) { this.users = users; }
    @Override public Optional<User> authenticate(String email, String password) {
        if (email == null || password == null) return Optional.empty();
        String normalized = email.trim().toLowerCase();
        for (com.healthsync.model.Role role : com.healthsync.model.Role.values()) {
            Optional<User> found = users.findByEmail(role, normalized);
            if (found.isPresent() && PasswordHasher.matches(password, found.get().getPasswordHash())) {
                SessionContext.getInstance().setCurrentUser(found.get());
                return found;
            }
        }
        return Optional.empty();
    }
    @Override public void logout() { SessionContext.getInstance().clear(); }
}