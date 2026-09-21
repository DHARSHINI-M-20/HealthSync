package com.healthsync.bridge;
/** Care-service abstraction; delivery can vary independently from the service type. */
public abstract class HealthcareService {
    protected final DeliveryMethod deliveryMethod;
    protected HealthcareService(DeliveryMethod deliveryMethod) { this.deliveryMethod=deliveryMethod; }
    public final void provideTo(String patientId) { deliveryMethod.deliver(patientId, serviceName()); }
    public abstract String serviceName();
}
