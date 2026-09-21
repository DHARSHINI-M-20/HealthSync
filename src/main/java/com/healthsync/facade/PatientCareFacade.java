package com.healthsync.facade;
import com.healthsync.model.MedicalRecord;
import com.healthsync.service.*;
/** Simplified consultation-completion workflow entry point. */
public class PatientCareFacade {
    private final MedicalRecordService records; private final BillingService billing; private final NotificationService notifications;
    public PatientCareFacade(MedicalRecordService records, BillingService billing, NotificationService notifications) { this.records=records; this.billing=billing; this.notifications=notifications; }
    public void completeConsultation(MedicalRecord record) { /* TODO: Persist record, issue invoice/prescription, update appointment, and notify patient. */ }
}
