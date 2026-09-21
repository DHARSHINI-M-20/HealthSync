package com.healthsync.chain;
import com.healthsync.model.EmergencyRequest;
/** Verifies that intake details exist before clinical triage. */
public class ReceptionEmergencyHandler extends EmergencyRequestHandler {
    protected void process(EmergencyRequest request) { if (request.getPatientId() == null || request.getPatientId().isBlank()) throw new IllegalArgumentException("Emergency request requires a patient"); request.addHandlingStep("Reception: intake registered"); }
}
