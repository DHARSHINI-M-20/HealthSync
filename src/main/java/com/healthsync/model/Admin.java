package com.healthsync.model;

/** Administrative account for system operations and configuration. */
public final class Admin extends User {
    public Admin(String id, String fullName, String email, String passwordHash) { super(id, fullName, email, passwordHash, Role.ADMIN); }
}
