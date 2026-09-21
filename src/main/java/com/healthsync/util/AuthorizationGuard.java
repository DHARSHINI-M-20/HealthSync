package com.healthsync.util;

import com.healthsync.model.Role;
import com.healthsync.model.User;

/** Application-layer authorization checks for protected use cases. */
public final class AuthorizationGuard {
    private AuthorizationGuard() {
    }

    public static User requireAny(Role... roles) {
        User user = SessionContext.getInstance().getCurrentUser()
                .orElseThrow(() -> new SecurityException("Authentication is required"));
        for (Role role : roles) {
            if (user.getRole() == role) return user;
        }
        throw new SecurityException("Your role is not allowed to perform this action");
    }
}