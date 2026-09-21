package com.healthsync.model;
import java.util.ArrayList;
import java.util.List;
/** Mutable request context progressing through emergency escalation handlers. */
public class EmergencyRequest {
    private final String patientId; private final String symptoms; private final int severity;
    private final List<String> handlingTrail=new ArrayList<>();
    public EmergencyRequest(String patientId, String symptoms, int severity) { this.patientId=patientId; this.symptoms=symptoms; this.severity=severity; }
    public String getPatientId() { return patientId; } public String getSymptoms() { return symptoms; } public int getSeverity() { return severity; }
    public void addHandlingStep(String step) { handlingTrail.add(step); }
    public List<String> getHandlingTrail() { return List.copyOf(handlingTrail); }
}
