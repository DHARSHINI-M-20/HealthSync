package com.healthsync.visitor;

import com.healthsync.model.Diagnosis;
import com.healthsync.model.MedicalReport;
import com.healthsync.model.Patient;
import com.healthsync.model.Prescription;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/** Computes a billing summary from visited patient-care entities. */
public class BillingVisitor implements HealthcareEntityVisitor {

    private static final Map<String, BigDecimal> MEDICATION_PRICES = new HashMap<>();
    static {
        MEDICATION_PRICES.put("PARA", new BigDecimal("5.00"));
        MEDICATION_PRICES.put("AMOX", new BigDecimal("12.50"));
        MEDICATION_PRICES.put("META", new BigDecimal("8.75"));
        MEDICATION_PRICES.put("ATOR", new BigDecimal("15.00"));
        MEDICATION_PRICES.put("LISI", new BigDecimal("22.00"));
    }

    private String patientId;
    private BigDecimal medicationTotal = BigDecimal.ZERO;
    private BigDecimal consultationFee = new BigDecimal("50.00");
    private final Map<String, BigDecimal> lineItems = new HashMap<>();

    @Override
    public void visitPatient(Patient patient) {
        this.patientId = patient.getPatientNumber();
        lineItems.put("Consultation fee", consultationFee);
    }

    @Override
    public void visitPrescription(Prescription prescription) {
        for (var item : prescription.getItems()) {
            BigDecimal price = MEDICATION_PRICES.getOrDefault(item.medication().code(), new BigDecimal("3.00"));
            BigDecimal line = price.multiply(new BigDecimal("1"));
            lineItems.put(item.medication().name() + " (" + item.dosage() + ")", line);
            medicationTotal = medicationTotal.add(line);
        }
    }

    @Override
    public void visitDiagnosis(Diagnosis diagnosis) {
        // Diagnosis itself carries no charge; kept for audit symmetry.
    }

    @Override
    public void visitMedicalReport(MedicalReport report) {
        // Report generation fee is folded into the consultation fee.
    }

    public String getPatientId() {
        return patientId;
    }

    public BigDecimal getMedicationTotal() {
        return medicationTotal.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getTotal() {
        return consultationFee.add(medicationTotal).setScale(2, RoundingMode.HALF_UP);
    }

    public Map<String, BigDecimal> getLineItems() {
        return Map.copyOf(lineItems);
    }
}