package com.healthsync.model;

/** Shared authenticated identity. */
public abstract class User {
    private String id;
    private String fullName;
    private String email;
    private String passwordHash;
    private final Role role;

    protected User(String id, String fullName, String email, String passwordHash, Role role) {
        this.id = id; this.fullName = fullName; this.email = email; this.passwordHash = passwordHash; this.role = role;
    }
    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Role getRole() { return role; }
    // TODO: Add controlled profile updates and persistence mapping.
}
