package com.healthsync.chain;
import com.healthsync.model.EmergencyRequest;
/** Final handler that routes the request to emergency-department care. */
public class EmergencyDepartmentHandler extends EmergencyRequestHandler { protected void process(EmergencyRequest request) { request.addHandlingStep("Emergency Department: case accepted"); } }
