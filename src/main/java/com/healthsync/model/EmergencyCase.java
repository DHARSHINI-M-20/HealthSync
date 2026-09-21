package com.healthsync.model;
/** Emergency admission and triage case. */
public class EmergencyCase {
    private final String id; private final String patientId; private final int priority;
    public EmergencyCase(String id, String patientId, int priority) { this.id = id; this.patientId = patientId; this.priority = priority; }
    public String getId() { return id; } public String getPatientId() { return patientId; } public int getPriority() { return priority; }
    // TODO: Add triage state and escalation history.
}
