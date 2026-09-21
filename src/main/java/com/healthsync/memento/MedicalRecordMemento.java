package com.healthsync.memento;
import com.healthsync.model.MedicalRecord;
/** Immutable snapshot of an entire medical-record version. */
public record MedicalRecordMemento(MedicalRecord record) { }
