package com.healthsync.chain;
import com.healthsync.model.EmergencyRequest;
/** Records nurse triage before physician review. */
public class NurseEmergencyHandler extends EmergencyRequestHandler { protected void process(EmergencyRequest request) { request.addHandlingStep("Nurse: triage severity " + request.getSeverity()); } }
