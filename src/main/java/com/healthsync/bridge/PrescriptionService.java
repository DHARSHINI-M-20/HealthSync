package com.healthsync.bridge;
public class PrescriptionService extends HealthcareService { public PrescriptionService(DeliveryMethod deliveryMethod) { super(deliveryMethod); } public String serviceName() { return "Prescription"; } }
