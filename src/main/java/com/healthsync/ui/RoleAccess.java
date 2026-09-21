package com.healthsync.ui;

import com.healthsync.model.Role;
import com.healthsync.model.User;

/** Central role policy used by screen routing and UI actions. */
public final class RoleAccess {
    private RoleAccess() {
    }

    public static boolean canOpen(String screen, User user) {
        if (ApplicationFrame.LOGIN.equals(screen) || ApplicationFrame.REGISTER.equals(screen)) {
            return user == null || user.getRole() == Role.ADMIN;
        }
        if (ApplicationFrame.GUEST_DASHBOARD.equals(screen)) return user == null;
        if (user == null) return false;
        if (user.getRole() == Role.ADMIN) return true;
        return switch (screen) {
            case ApplicationFrame.APPOINTMENTS, ApplicationFrame.NOTIFICATIONS, ApplicationFrame.EMERGENCY -> true;
            case ApplicationFrame.MEDICAL_RECORDS, ApplicationFrame.PRESCRIPTIONS -> true;
            case ApplicationFrame.PAYMENTS -> user.getRole() == Role.PATIENT;
            case ApplicationFrame.REPORTS -> user.getRole() == Role.DOCTOR;
            case ApplicationFrame.PATIENT_DASHBOARD -> user.getRole() == Role.PATIENT;
            case ApplicationFrame.DOCTOR_DASHBOARD -> user.getRole() == Role.DOCTOR;
            case ApplicationFrame.NURSE_DASHBOARD -> user.getRole() == Role.NURSE;
            default -> false;
        };
    }

    public static boolean hasAny(User user, Role... roles) {
        if (user == null) return false;
        for (Role role : roles) {
            if (user.getRole() == role) return true;
        }
        return false;
    }
}