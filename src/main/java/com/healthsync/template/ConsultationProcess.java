package com.healthsync.template;

import com.healthsync.model.MedicalRecord;
import com.healthsync.model.Prescription;
import java.util.List;

/** Template Method defining the invariant consultation workflow. */
public abstract class ConsultationProcess {

    public final void conduct(ConsultationContext context) {
        registerPatient(context);
        checkHistory(context);
        examination(context);
        diagnosis(context);
        prescription(context);
        completeConsultation(context);
    }

    protected void registerPatient(ConsultationContext context) {
        context.addStep("Patient registered");
    }

    protected void checkHistory(ConsultationContext context) {
        context.addStep("Medical history checked");
    }

    protected abstract void examination(ConsultationContext context);
    protected abstract void diagnosis(ConsultationContext context);
    protected abstract void prescription(ConsultationContext context);

    protected void completeConsultation(ConsultationContext context) {
        context.addStep("Consultation completed");
    }

    /** Hook: subclasses may attach a medical record produced during the consultation. */
    protected void persistRecord(ConsultationContext context, MedicalRecord record) {
        context.addStep("Medical record persisted for " + record.getRecordId());
    }

    /** Hook: subclasses may attach a prescription produced during the consultation. */
    protected void persistPrescription(ConsultationContext context, Prescription prescription) {
        context.addStep("Prescription persisted for " + prescription.getPatientId());
    }

    /** Hook: subclasses may customize the registration step. */
    protected void customizeRegistration(ConsultationContext context) { }

    /** Hook: subclasses may customize the completion step. */
    protected void customizeCompletion(ConsultationContext context) { }
}