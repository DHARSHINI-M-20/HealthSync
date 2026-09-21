package com.healthsync.ui;

import javax.swing.*;
import java.awt.*;

/** Role-aware dashboard shell that hosts quick links to the main functional screens. */
public class DashboardPanel extends JPanel {

    public DashboardPanel(AppContext context, String title, String selfScreen) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(30, 36, 30, 36));
        setBackground(UITheme.CANVAS);

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        JPanel copy = new JPanel();
        copy.setOpaque(false);
        copy.setLayout(new BoxLayout(copy, BoxLayout.Y_AXIS));
        copy.add(UITheme.eyebrow("HealthSync workspace"));
        copy.add(Box.createVerticalStrut(6));
        copy.add(UITheme.heading(title, 28));
        JLabel helper = new JLabel("A single view of the care journey, from first request to follow-up.");
        helper.setForeground(UITheme.MUTED);
        helper.setBorder(BorderFactory.createEmptyBorder(7, 0, 0, 0));
        copy.add(helper);
        header.add(copy, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 3, 16, 16));
        grid.setOpaque(false);
        grid.setBorder(BorderFactory.createEmptyBorder(28, 0, 0, 0));
        add(grid, BorderLayout.CENTER);

        boolean guest = selfScreen.equals(ApplicationFrame.GUEST_DASHBOARD);
        if (!guest) {
            addRoleLinks(grid, context, selfScreen);
        } else {
            link(grid, context, "Create an account", ApplicationFrame.REGISTER, "Set up a patient or care-team profile.");
            link(grid, context, "Sign in", ApplicationFrame.LOGIN, "Return to your secure workspace.");
        }

        if (ApplicationFrame.ADMIN_DASHBOARD.equals(selfScreen)) {
            link(grid, context, "Registration", ApplicationFrame.REGISTER, "Create a new user account.");
        }
    }

    private void addRoleLinks(JPanel grid, AppContext context, String roleScreen) {
        link(grid, context, "Appointments", ApplicationFrame.APPOINTMENTS, "Book, confirm, and complete consultations.");
        link(grid, context, "Medical Records", ApplicationFrame.MEDICAL_RECORDS, "View and create clinical records.");
        if (ApplicationFrame.PATIENT_DASHBOARD.equals(roleScreen)
                || ApplicationFrame.DOCTOR_DASHBOARD.equals(roleScreen)
                || ApplicationFrame.ADMIN_DASHBOARD.equals(roleScreen)) {
            link(grid, context, "Prescriptions", ApplicationFrame.PRESCRIPTIONS, "Issue and review medication directions.");
        }
        if (ApplicationFrame.PATIENT_DASHBOARD.equals(roleScreen)
                || ApplicationFrame.ADMIN_DASHBOARD.equals(roleScreen)) {
            link(grid, context, "Payments", ApplicationFrame.PAYMENTS, "Invoices and payment history.");
        }
        link(grid, context, "Notifications", ApplicationFrame.NOTIFICATIONS, "Send and review email receipts.");
        link(grid, context, "Emergency", ApplicationFrame.EMERGENCY, "Register and escalate urgent cases.");
        if (ApplicationFrame.DOCTOR_DASHBOARD.equals(roleScreen)
                || ApplicationFrame.ADMIN_DASHBOARD.equals(roleScreen)) {
            link(grid, context, "Reports", ApplicationFrame.REPORTS, "Clinical and billing summaries.");
        }
    }

    private void link(JPanel grid, AppContext context, String title, String screen, String desc) {
        JButton button = UITheme.button("<html><b>" + title + "</b><br><font color='#64748B'>" + desc + "</font></html>", false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setVerticalAlignment(SwingConstants.TOP);
        button.setPreferredSize(new Dimension(210, 86));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.BORDER),
            BorderFactory.createEmptyBorder(14, 16, 14, 16)));
        button.putClientProperty("screen", screen);
        button.setAlignmentX(JButton.LEFT);
        button.addActionListener(e -> context.appFrame.showScreen((String) button.getClientProperty("screen")));
        grid.add(button);
    }
}