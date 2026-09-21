package com.healthsync.visitor;
import com.healthsync.composite.ClinicalNote;
import com.healthsync.composite.RecordSection;
/** Adds reporting/audit operations to record component hierarchies. */
public interface MedicalRecordVisitor { void visitSection(RecordSection section); void visitNote(ClinicalNote note); }
