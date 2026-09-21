package com.healthsync;

import com.healthsync.ui.AppContext;
import com.healthsync.ui.ApplicationFrame;
import com.healthsync.ui.UITheme;

import javax.swing.SwingUtilities;

/**
 * Entry point for the HealthSync application.
 * Bootstraps the dependency graph (repositories -> services -> controllers -> UI)
 * and launches the Swing shell. No business logic lives here.
 */
public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        UITheme.install();
        SwingUtilities.invokeLater(() -> {
            AppContext context = new AppContext();
            // Ensure the JVM shuts down MongoDB cleanly on window close.
            Runtime.getRuntime().addShutdownHook(new Thread(context::shutdown));
            new ApplicationFrame(context).setVisible(true);
        });
    }
}