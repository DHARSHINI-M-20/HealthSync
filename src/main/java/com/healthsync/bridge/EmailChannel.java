package com.healthsync.bridge;

import com.healthsync.config.EmailConfiguration;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;
import java.util.regex.Pattern;

/** SMTP delivery adapter for HealthSync notifications. */
public class EmailChannel implements NotificationChannel {
	private static final Pattern EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
	private final EmailConfiguration configuration;

	public EmailChannel() {
		configuration = EmailConfiguration.load();
	}

	@Override
	public void deliver(String recipient, String subject, String body) {
		if (recipient == null || !EMAIL.matcher(recipient.trim()).matches()) {
			throw new IllegalArgumentException("Enter a valid email address, for example patient@example.com");
		}
		configuration.requireComplete();
		try {
			Properties properties = new Properties();
			properties.put("mail.smtp.host", configuration.getHost());
			properties.put("mail.smtp.port", String.valueOf(configuration.getPort()));
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.starttls.enable", String.valueOf(configuration.isStartTls()));
			properties.put("mail.smtp.starttls.required", String.valueOf(configuration.isStartTls()));
			Session session = Session.getInstance(properties, new Authenticator() {
				@Override protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(configuration.getUsername(), configuration.getPassword());
				}
			});

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(configuration.getFrom()));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient.trim(), true));
			message.setSubject(subject == null ? "HealthSync notification" : subject);
			message.setText(body == null ? "" : body);
			Transport.send(message);
		} catch (Exception exception) {
			throw new IllegalStateException("Email delivery failed: " + exception.getMessage(), exception);
		}
	}
}
