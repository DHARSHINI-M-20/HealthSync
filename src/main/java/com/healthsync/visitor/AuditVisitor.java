package com.healthsync.visitor;

import com.healthsync.model.Diagnosis;
import com.healthsync.model.MedicalReport;
import com.healthsync.model.Patient;
import com.healthsync.model.Prescription;

import java.util.ArrayList;
import java.util.List;

/** Produces an immutable audit trail of every entity visited. */
public class AuditVisitor implements HealthcareEntityVisitor {

    private final List<String> auditTrail = new ArrayList<>();

    @Override
    public void visitPatient(Patient patient) {
        auditTrail.add("AUDIT patient #" + patient.getPatientNumber()
                + " (" + patient.getFullName() + ") role=" + patient.getRole());
    }

    @Override
    public void visitPrescription(Prescription prescription) {
        auditTrail.add("AUDIT prescription #" + prescription.getId()
                + " patient #" + prescription.getPatientId()
                + " doctor #" + prescription.getDoctorId()
                + " items=" + prescription.getItems().size());
    }

    @Override
    public void visitDiagnosis(Diagnosis diagnosis) {
        auditTrail.add("AUDIT diagnosis code=" + diagnosis.code()
                + " description=" + diagnosis.description());
    }

    @Override
    public void visitMedicalReport(MedicalReport report) {
        auditTrail.add("AUDIT report #" + report.id()
                + " patient #" + report.patientId()
                + " title=" + report.title());
    }

    public List<String> getAuditTrail() {
        return List.copyOf(auditTrail);
    }
}