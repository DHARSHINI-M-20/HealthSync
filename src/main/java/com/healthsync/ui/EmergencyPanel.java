package com.healthsync.ui;

import com.healthsync.chain.EmergencyDepartmentHandler;
import com.healthsync.chain.EmergencyRequestHandler;
import com.healthsync.chain.GeneralDoctorEmergencyHandler;
import com.healthsync.chain.NurseEmergencyHandler;
import com.healthsync.chain.ReceptionEmergencyHandler;
import com.healthsync.chain.SpecialistEmergencyHandler;
import com.healthsync.model.EmergencyCase;
import com.healthsync.model.EmergencyRequest;
import com.healthsync.model.Role;
import com.healthsync.model.User;
import com.healthsync.service.EmergencyService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/** Emergency-management view backed by EmergencyService and the Chain of Responsibility. */
public class EmergencyPanel extends JPanel {

    private final AppContext context;
    private final JTextField patientField = new JTextField(18);
    private final JTextField symptomsField = new JTextField(18);
    private final JSpinner severitySpinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
    private final JButton registerButton = new JButton("Register emergency");
    private final JButton escalateButton = new JButton("Run chain");
    private final JTextArea trailArea = new JTextArea(8, 40);

    public EmergencyPanel(AppContext context) {
        this.context = context;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        form.add(new JLabel("Patient ID")); form.add(patientField);
        form.add(new JLabel("Symptoms")); form.add(symptomsField);
        form.add(new JLabel("Severity (1-10)")); form.add(severitySpinner);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.add(registerButton); actions.add(escalateButton);

        trailArea.setEditable(false);
        trailArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JPanel north = new JPanel(new BorderLayout());
        north.add(form, BorderLayout.CENTER);
        north.add(actions, BorderLayout.SOUTH);

        add(north, BorderLayout.NORTH);
        add(new JScrollPane(trailArea), BorderLayout.CENTER);

        registerButton.addActionListener(e -> {
            try {
                EmergencyCase emergencyCase = new EmergencyCase(null, patientField.getText(), (Integer) severitySpinner.getValue());
                context.emergency.register(emergencyCase);
                trailArea.append("Registered emergency case for patient " + patientField.getText() + "\n");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        escalateButton.addActionListener(e -> {
            User current = context.session.getCurrentUser().orElse(null);
            if (!RoleAccess.hasAny(current, Role.DOCTOR, Role.NURSE, Role.ADMIN)) {
                JOptionPane.showMessageDialog(this, "Only doctors, nurses, and administrators can escalate emergencies.", "Access restricted", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (patientField.getText().isBlank()) {
                JOptionPane.showMessageDialog(this, "Enter a patient ID first.");
                return;
            }
            EmergencyRequest request = new EmergencyRequest(patientField.getText(), symptomsField.getText(), (Integer) severitySpinner.getValue());
            EmergencyRequestHandler chain = new ReceptionEmergencyHandler()
                    .linkWith(new NurseEmergencyHandler())
                    .linkWith(new GeneralDoctorEmergencyHandler())
                    .linkWith(new SpecialistEmergencyHandler())
                    .linkWith(new EmergencyDepartmentHandler());
            chain.handle(request);
            trailArea.append("Handling trail:\n");
            request.getHandlingTrail().forEach(step -> trailArea.append("  - " + step + "\n"));
        });
    }
}