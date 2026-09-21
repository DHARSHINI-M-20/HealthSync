package com.healthsync.prototype;
import com.healthsync.model.RecordType;
/** Prototype defaults for follow-up consultations. */
public final class FollowUpConsultationTemplate extends MedicalRecordTemplate {
    public FollowUpConsultationTemplate() { super(RecordType.FOLLOW_UP); withNotes("Follow-up review of treatment progress."); }
}
