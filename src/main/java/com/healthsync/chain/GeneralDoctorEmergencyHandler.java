package com.healthsync.chain;
import com.healthsync.model.EmergencyRequest;
/** Performs initial medical assessment and triggers escalation when necessary. */
public class GeneralDoctorEmergencyHandler extends EmergencyRequestHandler { protected void process(EmergencyRequest request) { request.addHandlingStep("General Doctor: initial assessment"); } }
