package com.healthsync.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** Resolves SMTP settings without storing secrets in source control. */
public final class EmailConfiguration {
    private static final String HOST_KEY = "healthsync.mail.host";
    private static final String PORT_KEY = "healthsync.mail.port";
    private static final String USERNAME_KEY = "healthsync.mail.username";
    private static final String PASSWORD_KEY = "healthsync.mail.password";
    private static final String FROM_KEY = "healthsync.mail.from";
    private static final String TLS_KEY = "healthsync.mail.starttls";

    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final String from;
    private final boolean startTls;

    private EmailConfiguration(String host, int port, String username, String password, String from, boolean startTls) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.from = from;
        this.startTls = startTls;
    }

    public static EmailConfiguration load() {
        Properties properties = new Properties();
        try (InputStream stream = EmailConfiguration.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (stream != null) properties.load(stream);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to load email configuration", exception);
        }

        String username = value(USERNAME_KEY, "HEALTHSYNC_MAIL_USERNAME", properties);
        String from = firstNonBlank(value(FROM_KEY, "HEALTHSYNC_MAIL_FROM", properties), username);
        return new EmailConfiguration(
                value(HOST_KEY, "HEALTHSYNC_MAIL_HOST", properties),
                Integer.parseInt(firstNonBlank(value(PORT_KEY, "HEALTHSYNC_MAIL_PORT", properties), "587")),
                username,
                value(PASSWORD_KEY, "HEALTHSYNC_MAIL_PASSWORD", properties),
                from,
                Boolean.parseBoolean(firstNonBlank(value(TLS_KEY, "HEALTHSYNC_MAIL_STARTTLS", properties), "true")));
    }

    public String getHost() { return host; }
    public int getPort() { return port; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFrom() { return from; }
    public boolean isStartTls() { return startTls; }

    public void requireComplete() {
        if (host.isBlank() || username.isBlank() || password.isBlank() || from.isBlank()) {
            throw new IllegalStateException("SMTP is not configured. Set HEALTHSYNC_MAIL_HOST, HEALTHSYNC_MAIL_USERNAME, HEALTHSYNC_MAIL_PASSWORD, and HEALTHSYNC_MAIL_FROM (underscores, no spaces), then restart the application.");
        }
    }

    private static String value(String propertyKey, String environmentKey, Properties properties) {
        return firstNonBlank(System.getProperty(propertyKey), System.getenv(environmentKey), properties.getProperty(propertyKey), "");
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) if (value != null && !value.isBlank()) return value.trim();
        return "";
    }
}