package com.healthsync.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/** Shared visual language for the desktop application. */
public final class UITheme {
    public static final Color INK = new Color(0x172A3A);
    public static final Color MUTED = new Color(0x64748B);
    public static final Color TEAL = new Color(0x087F8C);
    public static final Color TEAL_DARK = new Color(0x075E68);
    public static final Color CORAL = new Color(0xE9785B);
    public static final Color SURFACE = new Color(0xFFFFFF);
    public static final Color CANVAS = new Color(0xF3F7F8);
    public static final Color BORDER = new Color(0xD7E2E5);

    private UITheme() {
    }

    public static void install() {
        UIManager.put("Panel.background", CANVAS);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", INK);
        UIManager.put("PasswordField.background", Color.WHITE);
        UIManager.put("PasswordField.foreground", INK);
        UIManager.put("Label.foreground", INK);
        UIManager.put("Button.background", Color.WHITE);
        UIManager.put("Button.foreground", INK);
        UIManager.put("Table.background", Color.WHITE);
        UIManager.put("Table.foreground", INK);
        UIManager.put("Table.gridColor", BORDER);
        UIManager.put("TableHeader.background", INK);
        UIManager.put("TableHeader.foreground", Color.WHITE);
        UIManager.put("ScrollPane.background", CANVAS);
        UIManager.put("TabbedPane.selected", Color.WHITE);
    }

    public static JLabel eyebrow(String text) {
        JLabel label = new JLabel(text.toUpperCase());
        label.setFont(new Font("SansSerif", Font.BOLD, 11));
        label.setForeground(TEAL);
        return label;
    }

    public static JLabel heading(String text, int size) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SansSerif", Font.BOLD, size));
        label.setForeground(INK);
        return label;
    }

    public static JButton button(String text, boolean primary) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 16, 10, 16));
        button.setOpaque(true);
        if (primary) {
            button.setBackground(TEAL);
            button.setForeground(Color.WHITE);
        }
        return button;
    }

    public static JPanel surface(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(18, 18, 18, 18)));
        return panel;
    }
}
