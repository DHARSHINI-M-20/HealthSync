package com.healthsync.model;

/** Nursing staff account with departmental assignment. */
public final class Nurse extends User {
    private final String nurseNumber;
    private final String department;
    public Nurse(String id, String fullName, String email, String passwordHash, String nurseNumber, String department) { super(id, fullName, email, passwordHash, Role.NURSE); this.nurseNumber=nurseNumber; this.department=department; }
    public String getNurseNumber() { return nurseNumber; }
    public String getDepartment() { return department; }
}
