package com.healthsync.ui;

import com.healthsync.model.Prescription;
import com.healthsync.model.PrescriptionItem;
import com.healthsync.model.Role;
import com.healthsync.model.User;
import com.healthsync.service.PrescriptionService;
import com.healthsync.flyweight.MedicationCatalog;
import com.healthsync.flyweight.MedicationCatalogItem;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/** Prescriptions view backed by PrescriptionService. */
public class PrescriptionsPanel extends JPanel {

    private final AppContext context;
    private final JTextField patientIdField = new JTextField();
    private final DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"ID", "Patient", "Doctor", "Issued", "Items"}, 0);
    private final JTable table = new JTable(tableModel);

    public PrescriptionsPanel(AppContext context) {
        this.context = context;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        JTextField doctorIdField = new JTextField();
        JTextField medicationField = new JTextField();
        JTextField dosageField = new JTextField();
        JTextField instructionsField = new JTextField();
        JButton issueButton = new JButton("Issue prescription");
        JButton refreshButton = new JButton("Refresh");

        form.add(new JLabel("Patient ID")); form.add(patientIdField);
        form.add(new JLabel("Doctor ID")); form.add(doctorIdField);
        form.add(new JLabel("Medication code")); form.add(medicationField);
        form.add(new JLabel("Dosage")); form.add(dosageField);
        form.add(new JLabel("Instructions")); form.add(instructionsField);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.add(issueButton); actions.add(refreshButton);

        JPanel north = new JPanel(new BorderLayout());
        north.add(form, BorderLayout.CENTER);
        north.add(actions, BorderLayout.SOUTH);

        table.setRowHeight(22);
        add(north, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        issueButton.addActionListener(e -> {
            try {
                User current = context.session.getCurrentUser().orElse(null);
                if (!RoleAccess.hasAny(current, Role.DOCTOR, Role.ADMIN)) {
                    throw new SecurityException("Only doctors and administrators can issue prescriptions.");
                }
                MedicationCatalog catalog = new MedicationCatalog();
                MedicationCatalogItem med = catalog.get(medicationField.getText(), medicationField.getText(), "Generic");
                List<PrescriptionItem> items = new ArrayList<>();
                items.add(new PrescriptionItem(med, dosageField.getText(), instructionsField.getText()));
                Prescription prescription = new Prescription(java.util.UUID.randomUUID().toString(), patientIdField.getText(), doctorIdField.getText(), Instant.now(), items);
                context.prescriptions.save(prescription);
                refresh();
                JOptionPane.showMessageDialog(this, "Prescription issued.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        refreshButton.addActionListener(e -> refresh());
        refresh();
    }

    private void refresh() {
        tableModel.setRowCount(0);
        String patientId = patientIdField.getText().trim();
        var prescriptions = patientId.isBlank()
                ? context.prescriptions.findAll()
                : context.prescriptions.findByPatientId(patientId);
        for (Prescription prescription : prescriptions) {
            tableModel.addRow(new Object[]{
                    prescription.getId(), prescription.getPatientId(), prescription.getDoctorId(),
                    prescription.getIssuedAt(), prescription.getItems().size()
            });
        }
    }
}