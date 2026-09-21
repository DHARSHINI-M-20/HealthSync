package com.healthsync.bridge;
/** Implementor in the healthcare-service delivery bridge. */
public interface DeliveryMethod { void deliver(String patientId, String serviceDescription); }
