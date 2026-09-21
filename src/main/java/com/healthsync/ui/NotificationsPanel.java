package com.healthsync.ui;

import com.healthsync.model.Notification;
import com.healthsync.service.NotificationService;

import javax.swing.*;
import java.awt.*;

/** Notifications view backed by NotificationService. */
public class NotificationsPanel extends JPanel {

    private final AppContext context;
    private final DefaultListModel<Notification> listModel = new DefaultListModel<>();
    private final JList<Notification> list = new JList<>(listModel);

    public NotificationsPanel(AppContext context) {
        this.context = context;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(28, 36, 28, 36));
        setBackground(UITheme.CANVAS);

        JPanel form = new JPanel(new GridLayout(0, 2, 6, 6));
        JTextField recipientField = new JTextField();
        JTextField messageField = new JTextField();
        JButton sendButton = UITheme.button("Send email", true);
        JButton refreshButton = UITheme.button("Refresh", false);

        JPanel heading = new JPanel();
        heading.setOpaque(false);
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.add(UITheme.eyebrow("Communication center"));
        heading.add(Box.createVerticalStrut(6));
        heading.add(UITheme.heading("Notifications", 28));
        JLabel helper = new JLabel("Send a validated email notification and keep a delivery receipt.");
        helper.setForeground(UITheme.MUTED);
        heading.add(helper);

        form.add(new JLabel("Recipient email")); form.add(recipientField);
        form.add(new JLabel("Message")); form.add(messageField);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.add(sendButton); actions.add(refreshButton);

        JPanel north = UITheme.surface(new BorderLayout(0, 14));
        north.add(heading, BorderLayout.NORTH);
        north.add(form, BorderLayout.CENTER);
        north.add(actions, BorderLayout.SOUTH);

        add(north, BorderLayout.NORTH);
        add(new JScrollPane(list), BorderLayout.CENTER);

        sendButton.addActionListener(e -> {
            try {
                context.notifications.notifyRecipient(recipientField.getText(), messageField.getText());
                refresh();
                JOptionPane.showMessageDialog(this, "Email accepted for delivery and saved as SENT.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Email not sent", JOptionPane.ERROR_MESSAGE);
            }
        });
        refreshButton.addActionListener(e -> refresh());
        refresh();
    }

    private void refresh() {
        listModel.clear();
        context.notifications.findAll().forEach(listModel::addElement);
    }
}