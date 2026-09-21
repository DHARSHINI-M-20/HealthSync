package com.healthsync.ui;

import com.healthsync.controller.MedicalRecordController;
import com.healthsync.builder.MedicalRecordBuilder;
import com.healthsync.model.MedicalRecord;
import com.healthsync.model.RecordType;
import com.healthsync.model.Role;
import com.healthsync.model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/** Medical-records view backed by MedicalRecordController and MedicalRecordBuilder. */
public class MedicalRecordsPanel extends JPanel {

    private final AppContext context;
    private final JTextField patientIdField = new JTextField();
    private final DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Record ID", "Patient", "Doctor", "Date", "Diagnosis", "Type"}, 0);
    private final JTable table = new JTable(tableModel);

    public MedicalRecordsPanel(AppContext context) {
        this.context = context;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        JTextField recordIdField = new JTextField();
        JTextField doctorIdField = new JTextField();
        JTextField diagnosisField = new JTextField();
        JTextField symptomsField = new JTextField();
        JTextField prescriptionField = new JTextField();
        JTextField notesField = new JTextField();
        JSpinner typeSpinner = new JSpinner(new SpinnerListModel(RecordType.values()));
        JButton saveButton = new JButton("Save record");
        JButton refreshButton = new JButton("Refresh");

        form.add(new JLabel("Record ID")); form.add(recordIdField);
        form.add(new JLabel("Patient ID")); form.add(patientIdField);
        form.add(new JLabel("Doctor ID")); form.add(doctorIdField);
        form.add(new JLabel("Diagnosis")); form.add(diagnosisField);
        form.add(new JLabel("Symptoms (comma)")); form.add(symptomsField);
        form.add(new JLabel("Prescription")); form.add(prescriptionField);
        form.add(new JLabel("Notes")); form.add(notesField);
        form.add(new JLabel("Record type")); form.add(typeSpinner);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.add(saveButton); actions.add(refreshButton);

        JPanel north = new JPanel(new BorderLayout());
        north.add(form, BorderLayout.CENTER);
        north.add(actions, BorderLayout.SOUTH);

        table.setRowHeight(22);
        add(north, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        saveButton.addActionListener(e -> {
            try {
                User current = context.session.getCurrentUser().orElse(null);
                if (!RoleAccess.hasAny(current, Role.DOCTOR, Role.ADMIN)) {
                    throw new SecurityException("Only doctors and administrators can create medical records.");
                }
                String recordId = recordIdField.getText().trim();
                if (recordId.isBlank()) recordId = java.util.UUID.randomUUID().toString();
                MedicalRecord record = new MedicalRecordBuilder()
                        .withRecordId(recordId)
                        .withPatientId(patientIdField.getText())
                        .withDoctorId(doctorIdField.getText())
                        .withDiagnosis(diagnosisField.getText())
                        .withSymptoms(java.util.Arrays.asList(symptomsField.getText().split(",")))
                        .withPrescription(prescriptionField.getText())
                        .withNotes(notesField.getText())
                        .withDate(java.time.Instant.now())
                        .withRecordType((RecordType) typeSpinner.getValue())
                        .build();
                context.recordController.save(record);
                refresh();
                JOptionPane.showMessageDialog(this, "Record saved.");
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
        User current = context.session.getCurrentUser().orElse(null);
        var records = current != null && current.getRole() == Role.PATIENT
            ? context.records.getPatientHistory(current.getId())
            : patientId.isBlank() ? context.records.findAll() : context.records.getPatientHistory(patientId);
        for (MedicalRecord record : records) {
            tableModel.addRow(new Object[]{
                    record.getRecordId(), record.getPatientId(), record.getDoctorId(),
                    record.getDate(), record.getDiagnosis(), record.getRecordType()
            });
        }
    }
}