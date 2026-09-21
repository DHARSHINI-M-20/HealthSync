package com.healthsync.ui;

import com.healthsync.model.User;

import javax.swing.*;
import java.awt.*;

/** Main desktop shell; views remain separate from controllers and services. */
public class ApplicationFrame extends JFrame {

    public static final String LOGIN = "LOGIN";
    public static final String REGISTER = "REGISTER";
    public static final String GUEST_DASHBOARD = "GUEST_DASHBOARD";
    public static final String PATIENT_DASHBOARD = "PATIENT_DASHBOARD";
    public static final String DOCTOR_DASHBOARD = "DOCTOR_DASHBOARD";
    public static final String NURSE_DASHBOARD = "NURSE_DASHBOARD";
    public static final String ADMIN_DASHBOARD = "ADMIN_DASHBOARD";
    public static final String APPOINTMENTS = "APPOINTMENTS";
    public static final String MEDICAL_RECORDS = "MEDICAL_RECORDS";
    public static final String PRESCRIPTIONS = "PRESCRIPTIONS";
    public static final String PAYMENTS = "PAYMENTS";
    public static final String NOTIFICATIONS = "NOTIFICATIONS";
    public static final String EMERGENCY = "EMERGENCY";
    public static final String REPORTS = "REPORTS";

    private final AppContext context;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);
    private final JButton logoutButton = UITheme.button("Sign out", false);
    private final JButton backButton = UITheme.button("Back to dashboard", false);
    private final JLabel userLabel = new JLabel(" ");

    public ApplicationFrame(AppContext context) {
        super("HealthSync – Healthcare Management and Patient Care System");
        this.context = context;
        context.appFrame = this;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(980, 640));
        setSize(1180, 760);
        setLocationRelativeTo(null);

        cardPanel.add(new LoginPanel(context), LOGIN);
        cardPanel.add(new RegistrationPanel(context), REGISTER);
        cardPanel.add(new DashboardPanel(context, "Guest Dashboard", GUEST_DASHBOARD), GUEST_DASHBOARD);
        cardPanel.add(new DashboardPanel(context, "Patient Dashboard", PATIENT_DASHBOARD), PATIENT_DASHBOARD);
        cardPanel.add(new DashboardPanel(context, "Doctor Dashboard", DOCTOR_DASHBOARD), DOCTOR_DASHBOARD);
        cardPanel.add(new DashboardPanel(context, "Nurse Dashboard", NURSE_DASHBOARD), NURSE_DASHBOARD);
        cardPanel.add(new DashboardPanel(context, "Admin Dashboard", ADMIN_DASHBOARD), ADMIN_DASHBOARD);
        cardPanel.add(new AppointmentPanel(context), APPOINTMENTS);
        cardPanel.add(new MedicalRecordsPanel(context), MEDICAL_RECORDS);
        cardPanel.add(new PrescriptionsPanel(context), PRESCRIPTIONS);
        cardPanel.add(new PaymentsPanel(context), PAYMENTS);
        cardPanel.add(new NotificationsPanel(context), NOTIFICATIONS);
        cardPanel.add(new EmergencyPanel(context), EMERGENCY);
        cardPanel.add(new ReportsPanel(context), REPORTS);

        JPanel north = new JPanel(new BorderLayout(20, 0));
        north.setBackground(UITheme.SURFACE);
        north.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, UITheme.BORDER),
            BorderFactory.createEmptyBorder(12, 20, 12, 20)));
        JLabel brand = UITheme.heading("HealthSync", 20);
        brand.setForeground(UITheme.TEAL_DARK);
        north.add(brand, BorderLayout.WEST);
        userLabel.setForeground(UITheme.MUTED);
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        north.add(userLabel, BorderLayout.CENTER);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        actions.add(backButton);
        actions.add(logoutButton);
        north.add(actions, BorderLayout.EAST);

        logoutButton.addActionListener(e -> {
            context.authController.logout();
            userLabel.setText(" ");
            showScreen(LOGIN);
        });
        backButton.addActionListener(e -> showScreen(dashboardForCurrentUser()));

        setLayout(new BorderLayout());
        add(north, BorderLayout.NORTH);
        add(cardPanel, BorderLayout.CENTER);

        showScreen(LOGIN);
    }

    public void showScreen(String name) {
        User current = context.session.getCurrentUser().orElse(null);
        if (!RoleAccess.canOpen(name, current)) {
            JOptionPane.showMessageDialog(this,
                    "Your role does not have access to this workspace.",
                    "Access restricted", JOptionPane.WARNING_MESSAGE);
            return;
        }
        cardLayout.show(cardPanel, name);
        userLabel.setText(current == null ? "Not signed in" : "Signed in as " + current.getFullName() + " (" + current.getRole() + ")");
        logoutButton.setVisible(current != null);
        backButton.setVisible(current != null && !isDashboard(name));
    }

    private boolean isDashboard(String name) {
        return GUEST_DASHBOARD.equals(name) || PATIENT_DASHBOARD.equals(name)
                || DOCTOR_DASHBOARD.equals(name) || NURSE_DASHBOARD.equals(name)
                || ADMIN_DASHBOARD.equals(name) || LOGIN.equals(name) || REGISTER.equals(name);
    }

    private String dashboardForCurrentUser() {
        User current = context.session.getCurrentUser().orElse(null);
        if (current == null) return LOGIN;
        return switch (current.getRole()) {
            case PATIENT -> PATIENT_DASHBOARD;
            case DOCTOR -> DOCTOR_DASHBOARD;
            case NURSE -> NURSE_DASHBOARD;
            case ADMIN -> ADMIN_DASHBOARD;
        };
    }
}