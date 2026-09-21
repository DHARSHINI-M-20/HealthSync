package com.healthsync.bridge;
public class MedicalTestService extends HealthcareService { public MedicalTestService(DeliveryMethod deliveryMethod) { super(deliveryMethod); } public String serviceName() { return "Medical Test"; } }
