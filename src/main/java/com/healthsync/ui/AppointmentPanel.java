package com.healthsync.ui;

import com.healthsync.controller.AppointmentController;
import com.healthsync.model.Appointment;
import com.healthsync.model.Role;
import com.healthsync.model.User;
import com.healthsync.strategy.AppointmentPriorityStrategy;
import com.healthsync.strategy.EmergencyPriority;
import com.healthsync.strategy.FollowUpPriority;
import com.healthsync.strategy.NormalPriority;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.Instant;
import java.util.Date;

/** Appointment-management view backed by AppointmentController. */
public class AppointmentPanel extends JPanel {

    private final AppContext context;
    private final DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"ID", "Patient", "Doctor", "When", "Priority", "Status"}, 0);
    private final JTable table = new JTable(tableModel);

    public AppointmentPanel(AppContext context) {
        this.context = context;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        JTextField patientField = new JTextField();
        JTextField doctorField = new JTextField();
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd HH:mm");
        dateSpinner.setEditor(dateEditor);
        JSpinner prioritySpinner = new JSpinner(new SpinnerListModel(new Object[]{"NORMAL", "FOLLOW_UP", "EMERGENCY"}));
        JButton bookButton = new JButton("Book appointment");
        JButton confirmButton = new JButton("Confirm");
        JButton completeButton = new JButton("Complete");
        JButton checkInButton = new JButton("Check in");
        JButton rejectButton = new JButton("Reject");
        JButton refreshButton = new JButton("Refresh");

        form.add(new JLabel("Patient ID")); form.add(patientField);
        form.add(new JLabel("Doctor ID")); form.add(doctorField);
        form.add(new JLabel("Scheduled at")); form.add(dateSpinner);
        form.add(new JLabel("Priority")); form.add(prioritySpinner);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.add(bookButton); actions.add(confirmButton); actions.add(completeButton);
        actions.add(checkInButton); actions.add(rejectButton); actions.add(refreshButton);

        JPanel north = new JPanel(new BorderLayout());
        north.add(form, BorderLayout.CENTER);
        north.add(actions, BorderLayout.SOUTH);

        table.setRowHeight(22);
        add(north, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        bookButton.addActionListener(e -> {
            try {
                Instant when = ((Date) dateSpinner.getValue()).toInstant();
                String priority = (String) prioritySpinner.getValue();
                AppointmentPriorityStrategy strategy = switch (priority) {
                    case "EMERGENCY" -> new EmergencyPriority();
                    case "FOLLOW_UP" -> new FollowUpPriority();
                    default -> new NormalPriority();
                };
                Appointment appointment = context.appointmentController.book(
                        patientField.getText(), doctorField.getText(), when, strategy);
                if (appointment != null) refresh();
                else JOptionPane.showMessageDialog(this, "Booking failed.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
        confirmButton.addActionListener(e -> transition("confirm"));
        completeButton.addActionListener(e -> transition("complete"));
        checkInButton.addActionListener(e -> transition("checkIn"));
        rejectButton.addActionListener(e -> transition("reject"));
        refreshButton.addActionListener(e -> refresh());

        refresh();
    }

    private void transition(String action) {
        User current = context.session.getCurrentUser().orElse(null);
        if ("confirm".equals(action) || "complete".equals(action) || "checkIn".equals(action) || "reject".equals(action)) {
            if (!RoleAccess.hasAny(current, Role.DOCTOR, Role.NURSE, Role.ADMIN)) {
                JOptionPane.showMessageDialog(this, "Only doctors, nurses, and administrators can update appointment status.", "Access restricted", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Select an appointment first."); return; }
        String id = (String) tableModel.getValueAt(row, 0);
        try {
            switch (action) {
                case "confirm" -> context.appointmentController.confirm(id);
                case "complete" -> context.appointmentController.complete(id);
                case "checkIn" -> context.appointmentController.checkIn(id);
                case "reject" -> context.appointmentController.reject(id);
            }
            refresh();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private void refresh() {
        tableModel.setRowCount(0);
        for (Appointment appointment : context.appointments.findAll()) {
            tableModel.addRow(new Object[]{
                    appointment.getId(), appointment.getPatientId(), appointment.getDoctorId(),
                    appointment.getScheduledAt(), appointment.getPriorityType(), appointment.getStatus()
            });
        }
    }
}