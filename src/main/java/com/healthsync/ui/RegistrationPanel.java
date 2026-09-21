package com.healthsync.ui;

import com.healthsync.model.Role;
import com.healthsync.service.UserManagementService;
import com.healthsync.service.UserRegistrationRequest;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/** Registration view. Delegates all validation and persistence to the controller/service layer. */
public class RegistrationPanel extends JPanel {

    private final AppContext context;
    private final JTextField nameField = new JTextField(18);
    private final JTextField emailField = new JTextField(18);
    private final JPasswordField passwordField = new JPasswordField(18);
    private final JTextField patientField = new JTextField(14);
    private final JTextField licenseField = new JTextField(14);
    private final JTextField specialtyField = new JTextField(14);
    private final JTextField nurseField = new JTextField(14);
    private final JTextField departmentField = new JTextField(14);
    private final JLabel statusLabel = new JLabel(" ");
    private final JButton submitButton = new JButton("Create account");
    private final JButton backButton = new JButton("Back to login");

    public RegistrationPanel(AppContext context) {
        this.context = context;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(20, 40, 20, 40));

        JPanel form = new JPanel(new GridBagLayout());
        int y = 0;
        addRow(form, "Full name *", nameField, y++);
        addRow(form, "Email *", emailField, y++);
        addRow(form, "Password * (8+ chars)", passwordField, y++);
        addRow(form, "Patient number", patientField, y++);
        addRow(form, "License number", licenseField, y++);
        addRow(form, "Specialty code", specialtyField, y++);
        addRow(form, "Nurse number", nurseField, y++);
        addRow(form, "Department", departmentField, y++);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.add(submitButton);
        actions.add(backButton);

        JPanel south = new JPanel(new BorderLayout());
        south.add(actions, BorderLayout.CENTER);
        south.add(statusLabel, BorderLayout.SOUTH);

        add(form, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);

        submitButton.addActionListener(e -> {
            statusLabel.setText(" ");
            try {
                int roleIndex = JOptionPane.showOptionDialog(this, "Select role", "Role",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                        new Object[]{"Patient", "Doctor", "Nurse", "Admin"}, "Patient");
                if (roleIndex < 0) return;
                Role selected = switch (roleIndex) {
                    case 1 -> Role.DOCTOR;
                    case 2 -> Role.NURSE;
                    case 3 -> Role.ADMIN;
                    default -> Role.PATIENT;
                };
                context.users.register(new UserRegistrationRequest(
                        nameField.getText(), emailField.getText(), new String(passwordField.getPassword()), selected,
                        patientField.getText(), licenseField.getText(), specialtyField.getText(),
                        nurseField.getText(), departmentField.getText()));
                JOptionPane.showMessageDialog(this, "Account created. Please sign in.");
                context.appFrame.showScreen(ApplicationFrame.LOGIN);
            } catch (Exception ex) {
                statusLabel.setText("Error: " + ex.getMessage());
            }
        });
        backButton.addActionListener(e -> context.appFrame.showScreen(ApplicationFrame.LOGIN));
    }

    private void addRow(JPanel form, String label, JComponent field, int y) {
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(3, 3, 3, 3);
        g.gridx = 0; g.gridy = y; g.anchor = GridBagConstraints.EAST;
        form.add(new JLabel(label), g);
        g.gridx = 1; g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1.0;
        form.add(field, g);
    }
}