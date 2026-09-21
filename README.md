# HealthSync

HealthSync is a Java 17 desktop healthcare management system. It gives patients and care teams one workspace for scheduling appointments, maintaining medical records, issuing prescriptions, tracking payments, sending notifications, handling emergencies, and generating reports.

The project is also a practical demonstration of object-oriented design patterns in a real application: factories, builder, adapter, bridge, chain of responsibility, command, composite, decorator, facade, flyweight, interpreter, iterator, mediator, memento, observer, prototype, proxy, state, strategy, template method, and visitor.

## Project Overview

- **Interface:** Java Swing desktop application with role-aware dashboards for patients, doctors, nurses, and administrators.
- **Application layer:** Controllers coordinate authentication, appointments, patient records, and user registration.
- **Persistence:** MongoDB stores users, appointments, records, prescriptions, invoices, payments, notifications, and emergency cases.
- **Configuration:** `src/main/resources/application.properties` contains the local MongoDB defaults.
- **Tests:** JUnit 5 unit tests cover the core patterns and services. MongoDB CRUD coverage is opt-in.

## Requirements

- Java 17 or newer
- Maven 3.9 or newer
- MongoDB running locally on `mongodb://localhost:27017`, or a compatible MongoDB connection string

## Run It

1. Start MongoDB.
2. From the project folder, run:

	```powershell
	mvn clean compile
	mvn exec:java
	```

	The HealthSync desktop window opens after the application starts.

To use another MongoDB instance, set an environment variable before launching:

```powershell
$env:HEALTHSYNC_MONGODB_URI = "mongodb://username:password@host:27017"
mvn exec:java
```

The database name defaults to `healthsync`. It can be changed with `healthsync.mongodb.database` in `src/main/resources/application.properties`.

## Test It

Run the automated unit suite:

```powershell
mvn test
```

Run the MongoDB CRUD integration test as well:

```powershell
mvn test -Dmongo.integration=true
```

## Inputs and Workflows

### Sign in and registration

- **Sign in:** email and password for an existing account.
- **Create account:** full name, email, password of at least 8 characters, role, and role-specific identifiers such as patient number, license number, specialty, nurse number, or department.
- **Guest mode:** opens a public dashboard with links to sign in or create an account.

### Appointments

Enter patient ID, doctor ID, scheduled date and time, and priority (`NORMAL`, `FOLLOW_UP`, or `EMERGENCY`). Select an appointment to confirm, complete, check in, or reject it.

### Medical records

Patients can view their own records. Doctors and administrators can create records by entering record ID (optional), patient ID, doctor ID, diagnosis, comma-separated symptoms, prescription, notes, and a record type. The system timestamps and persists the record.

### Prescriptions

Patients can review prescriptions. Doctors and administrators can issue them by entering patient ID, doctor ID, medication code, dosage, and instructions. The medication catalog reuses medication definitions by code.

### Payments

Enter an invoice ID, amount, and payment method (`CASH`, `CARD`, or `INSURANCE`). Payments are recorded against an existing invoice.

### Notifications and emergencies

- **Notifications:** recipient email address and message. The application validates the email, sends it through the configured SMTP provider, and saves a `SENT` delivery receipt only after the provider accepts the message.
- **Emergency:** patient ID, symptoms, and severity from 1 to 10. Register the case, then run the emergency handling chain.

Use **Back to dashboard** in the top bar from any workspace screen. After sign-in, the dashboard is role-aware: patients see care and payment tools, doctors see clinical and reporting tools, nurses see care coordination tools, and administrators see the full operational workspace.

### Reports

The Reports screen generates a clinical report, computes billing, or displays an audit trail using visitor-based reporting.

## Notes

The application expects MongoDB to be available when the desktop application starts because the repositories are wired during application bootstrap. The MongoDB integration test is skipped by default so the regular unit suite can run without a database server.

## Real Email Setup

Configure SMTP before starting the application. Use environment variables so the SMTP password is not committed:

```powershell
$env:HEALTHSYNC_MAIL_HOST = "smtp.gmail.com"
$env:HEALTHSYNC_MAIL_PORT = "587"
$env:HEALTHSYNC_MAIL_USERNAME = "your-account@gmail.com"
$env:HEALTHSYNC_MAIL_PASSWORD = "your-provider-app-password"
$env:HEALTHSYNC_MAIL_FROM = "your-account@gmail.com"
$env:HEALTHSYNC_MAIL_STARTTLS = "true"
mvn exec:java
```

PowerShell environment variables apply to the current terminal session. Run the commands in the same terminal where you run Maven. The variable name is `HEALTHSYNC_MAIL_FROM`, with underscores between every word. For a one-time launch, use Maven system properties instead:

```powershell
mvn exec:java `
	"-Dhealthsync.mail.host=smtp.gmail.com" `
	"-Dhealthsync.mail.port=587" `
	"-Dhealthsync.mail.username=your-account@gmail.com" `
	"-Dhealthsync.mail.password=your-provider-app-password" `
	"-Dhealthsync.mail.from=your-account@gmail.com" `
	"-Dhealthsync.mail.starttls=true"
```

Do not put the real password in `application.properties` or commit it to Git. If you still see the SMTP error, verify that all six setup commands ran successfully with `Get-ChildItem Env:HEALTHSYNC_MAIL_*`, then restart `mvn exec:java`.

For Gmail, use a Google app password rather than your normal account password. For Outlook, Microsoft 365, SendGrid, Mailgun, or another provider, use that provider's SMTP host, port, username, and password/API SMTP credential. The notification screen reports a delivery failure and does not save `SENT` when SMTP rejects the message.
