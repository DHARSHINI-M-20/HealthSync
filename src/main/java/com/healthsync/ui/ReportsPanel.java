package com.healthsync.ui;

import com.healthsync.visitor.AuditVisitor;
import com.healthsync.visitor.BillingVisitor;
import com.healthsync.visitor.ReportVisitor;
import com.healthsync.model.Diagnosis;
import com.healthsync.model.MedicalReport;
import com.healthsync.model.Patient;
import com.healthsync.model.Prescription;
import com.healthsync.model.Role;
import com.healthsync.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/** Reports view that exercises the Visitor pattern across patient-care entities. */
public class ReportsPanel extends JPanel {

    private final AppContext context;
    private final JButton reportButton = new JButton("Generate report");
    private final JButton billingButton = new JButton("Compute billing");
    private final JButton auditButton = new JButton("Run audit");
    private final JTextArea outputArea = new JTextArea(12, 60);

    public ReportsPanel(AppContext context) {
        this.context = context;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.add(reportButton); actions.add(billingButton); actions.add(auditButton);

        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        add(actions, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);

        Patient samplePatient = new Patient("P-1001", "Ada Lovelace", "ada@example.com", "x", "P-1001");
        Prescription sampleRx = new Prescription("RX-1", "P-1001", "D-1", java.time.Instant.now(),
                java.util.List.of(new com.healthsync.model.PrescriptionItem(
                        new com.healthsync.flyweight.MedicationCatalogItem("PARA", "Paracetamol", "500mg"), "500mg", "after meals")));
        Diagnosis sampleDiagnosis = new Diagnosis("J00", "Acute bronchitis");
        MedicalReport sampleReport = new MedicalReport("RPT-1", "P-1001", "Discharge summary");

        reportButton.addActionListener(e -> {
            if (!canReport()) return;
            ReportVisitor visitor = new ReportVisitor();
            samplePatient.accept(visitor);
            sampleRx.accept(visitor);
            sampleDiagnosis.accept(visitor);
            sampleReport.accept(visitor);
            outputArea.setText(visitor.render());
        });

        billingButton.addActionListener(e -> {
            if (!canReport()) return;
            BillingVisitor visitor = new BillingVisitor();
            samplePatient.accept(visitor);
            sampleRx.accept(visitor);
            sampleDiagnosis.accept(visitor);
            sampleReport.accept(visitor);
            StringBuilder sb = new StringBuilder("Billing for patient " + visitor.getPatientId() + "\n");
            visitor.getLineItems().forEach((k, v) -> sb.append("  ").append(k).append(": $").append(v).append("\n"));
            sb.append("Medication total: $").append(visitor.getMedicationTotal()).append("\n");
            sb.append("Grand total: $").append(visitor.getTotal());
            outputArea.setText(sb.toString());
        });

        auditButton.addActionListener(e -> {
            if (!canReport()) return;
            AuditVisitor visitor = new AuditVisitor();
            samplePatient.accept(visitor);
            sampleRx.accept(visitor);
            sampleDiagnosis.accept(visitor);
            sampleReport.accept(visitor);
            StringBuilder sb = new StringBuilder("Audit trail:\n");
            visitor.getAuditTrail().forEach(line -> sb.append("  ").append(line).append("\n"));
            outputArea.setText(sb.toString());
        });
    }

    private boolean canReport() {
        User current = context.session.getCurrentUser().orElse(null);
        if (RoleAccess.hasAny(current, Role.DOCTOR, Role.ADMIN)) return true;
        JOptionPane.showMessageDialog(this, "Only doctors and administrators can generate reports.", "Access restricted", JOptionPane.WARNING_MESSAGE);
        return false;
    }
}