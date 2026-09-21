package com.healthsync.ui;

import com.healthsync.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** Login view. Knows only about text fields and buttons; the controller owns the use case. */
public class LoginPanel extends JPanel {

    private final AppContext context;
    private final JTextField emailField = new JTextField(22);
    private final JPasswordField passwordField = new JPasswordField(22);
    private final JButton loginButton = new JButton("Sign in");
    private final JButton registerButton = new JButton("Create account");
    private final JButton guestButton = new JButton("Continue as guest");
    private final JLabel statusLabel = new JLabel(" ");

    public LoginPanel(AppContext context) {
        this.context = context;
        setLayout(new GridLayout(1, 2));
        setBackground(UITheme.CANVAS);

        JPanel welcome = new JPanel(new GridBagLayout());
        welcome.setBackground(UITheme.TEAL_DARK);
        welcome.setBorder(BorderFactory.createEmptyBorder(48, 56, 48, 56));
        JPanel welcomeCopy = new JPanel();
        welcomeCopy.setOpaque(false);
        welcomeCopy.setLayout(new BoxLayout(welcomeCopy, BoxLayout.Y_AXIS));
        JLabel mark = UITheme.heading("HS", 42);
        mark.setForeground(UITheme.CORAL);
        JLabel title = UITheme.heading("Care, connected.", 34);
        title.setForeground(Color.WHITE);
        JLabel subtitle = new JLabel("One calm workspace for appointments, records, prescriptions, payments, and urgent care.");
        subtitle.setForeground(new Color(0xD8F1F2));
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 15));
        subtitle.setMaximumSize(new Dimension(360, 60));
        welcomeCopy.add(mark);
        welcomeCopy.add(Box.createVerticalStrut(26));
        welcomeCopy.add(title);
        welcomeCopy.add(Box.createVerticalStrut(14));
        welcomeCopy.add(subtitle);
        welcome.add(welcomeCopy);

        JPanel formShell = new JPanel(new GridBagLayout());
        formShell.setBackground(UITheme.CANVAS);
        JPanel card = UITheme.surface(new GridBagLayout());
        card.setPreferredSize(new Dimension(430, 330));
        GridBagConstraints cardConstraints = new GridBagConstraints();
        cardConstraints.fill = GridBagConstraints.HORIZONTAL;
        cardConstraints.weightx = 1;
        cardConstraints.insets = new Insets(7, 8, 7, 8);
        GridBagConstraints titleConstraints = (GridBagConstraints) cardConstraints.clone();
        titleConstraints.gridy = 0;
        card.add(UITheme.eyebrow("Welcome back"), titleConstraints);
        JLabel heading = UITheme.heading("Sign in to HealthSync", 25);
        titleConstraints.gridy = 1;
        card.add(heading, titleConstraints);
        addLabeled(card, 2, "Email", emailField);
        addLabeled(card, 4, "Password", passwordField);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.setOpaque(false);
        loginButton.setBackground(UITheme.TEAL);
        loginButton.setForeground(Color.WHITE);
        actions.add(loginButton);
        actions.add(registerButton);
        actions.add(guestButton);

        JPanel south = new JPanel(new BorderLayout(0, 8));
        south.setOpaque(false);
        south.add(actions, BorderLayout.CENTER);
        south.add(statusLabel, BorderLayout.SOUTH);
        GridBagConstraints southConstraints = (GridBagConstraints) cardConstraints.clone();
        southConstraints.gridy = 6;
        card.add(south, southConstraints);
        formShell.add(card);
        add(welcome);
        add(formShell);

        loginButton.addActionListener(new LoginAction());
        registerButton.addActionListener(e -> context.appFrame.showScreen(ApplicationFrame.REGISTER));
        guestButton.addActionListener(e -> context.appFrame.showScreen(ApplicationFrame.GUEST_DASHBOARD));
    }

    private void addLabeled(JPanel form, int row, String label, JComponent field) {
        GridBagConstraints g = new GridBagConstraints();
        g.gridy = row;
        g.insets = new Insets(6, 8, 2, 8);
        g.anchor = GridBagConstraints.WEST;
        g.gridwidth = 2;
        form.add(new JLabel(label), g);
        g.gridy = row + 1;
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1.0;
        g.insets = new Insets(0, 8, 6, 8);
        form.add(field, g);
    }

    private class LoginAction implements ActionListener {
        @Override public void actionPerformed(ActionEvent e) {
            statusLabel.setText(" ");
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            try {
                User user = context.authController.login(email, password);
                if (user != null) {
                    context.appFrame.showScreen(dashboardFor(user));
                } else {
                    statusLabel.setText("Invalid email or password.");
                }
            } catch (Exception ex) {
                statusLabel.setText("Error: " + ex.getMessage());
            }
        }
    }

    private String dashboardFor(User user) {
        return switch (user.getRole()) {
            case PATIENT -> ApplicationFrame.PATIENT_DASHBOARD;
            case DOCTOR -> ApplicationFrame.DOCTOR_DASHBOARD;
            case NURSE -> ApplicationFrame.NURSE_DASHBOARD;
            case ADMIN -> ApplicationFrame.ADMIN_DASHBOARD;
        };
    }
}