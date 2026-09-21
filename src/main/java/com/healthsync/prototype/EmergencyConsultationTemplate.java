package com.healthsync.prototype;
import com.healthsync.model.RecordType;
/** Prototype defaults for urgent emergency consultations. */
public final class EmergencyConsultationTemplate extends MedicalRecordTemplate {
    public EmergencyConsultationTemplate() { super(RecordType.EMERGENCY_CONSULTATION); withNotes("Emergency assessment and stabilization."); }
}
