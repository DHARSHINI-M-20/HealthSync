package com.healthsync.interpreter;

import com.healthsync.model.Appointment;
import com.healthsync.model.AppointmentStatus;

/** Parses a limited report-query language built from composite expression nodes. */
public class ReportQueryInterpreter {

    public ReportExpression parse(String query) {
        if (query == null || query.isBlank()) return appointment -> true;
        String[] andParts = query.toUpperCase().split("\\s+AND\\s+");
        ReportExpression result = null;
        for (String part : andParts) {
            ReportExpression term = parseTerm(part.trim());
            result = result == null ? term : new AndExpression(result, term);
        }
        return result == null ? (appointment -> true) : result;
    }

    private ReportExpression parseTerm(String term) {
        String[] eq = term.split("\\s*=\\s*", 2);
        if (eq.length == 2) {
            String field = eq[0].trim();
            String value = eq[1].trim();
            return switch (field) {
                case "DOCTOR" -> new DoctorExpression(value);
                case "PATIENT" -> new PatientExpression(value);
                case "STATUS" -> new StatusExpression(AppointmentStatus.valueOf(value));
                default -> throw new IllegalArgumentException("Unknown field: " + field);
            };
        }
        String[] gt = term.split("\\s+>\\s+", 2);
        if (gt.length == 2) {
            String field = gt[0].trim();
            int threshold = Integer.parseInt(gt[1].trim());
            return switch (field) {
                case "PRIORITY" -> appointment -> appointment.getPriorityScore() > threshold;
                default -> throw new IllegalArgumentException("Unknown numeric field: " + field);
            };
        }
        throw new IllegalArgumentException("Unsupported term: " + term);
    }
}