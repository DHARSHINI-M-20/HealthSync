package com.healthsync.bridge;
public class ConsultationService extends HealthcareService { public ConsultationService(DeliveryMethod deliveryMethod) { super(deliveryMethod); } public String serviceName() { return "Consultation"; } }
