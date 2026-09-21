package com.healthsync.chain;
import com.healthsync.model.EmergencyRequest;
/** Adds specialist review for high-severity requests. */
public class SpecialistEmergencyHandler extends EmergencyRequestHandler { protected void process(EmergencyRequest request) { if (request.getSeverity() >= 4) request.addHandlingStep("Specialist: urgent review"); else request.addHandlingStep("Specialist: review not required"); } }
