# HealthSync Test Plan

## 1. Scope and test levels

- **Unit tests:** deterministic pattern classes, models, state transitions, validation, authorization, and service behavior with in-memory repositories.
- **Integration tests:** MongoDB repositories against a disposable or dedicated test database. These use `HEALTHSYNC_MONGODB_URI` and `HEALTHSYNC_MONGODB_DATABASE` and must clean up test documents.
- **UI smoke tests:** construct each Swing panel on the EDT and exercise controller-facing actions with test collaborators. Do not require a visible display in headless CI.
- **Regression gate:** `mvn clean test` must pass. MongoDB tests are enabled separately with `-Dmongo.integration=true`.

## 2. GoF pattern coverage matrix

| Pattern | Implementation class(es) | Feature using it | Test scenario | Expected behavior | Possible failure cases |
|---|---|---|---|---|---|
| Abstract Factory | `HospitalFactory`, `ClinicFactory`, `HealthcareFactory` | Creates compatible appointment, prescription, and notification products | Create the three products from each factory | Every product belongs to the selected care-environment family and implements its product interface | Mixed-family products, null factory, incomplete product family |
| Adapter | `PaymentGatewayAdapter`, `InsurancePaymentAdapter` | Unifies external/card/insurance payment APIs | Process an approved and declined payment request | Unified result preserves reference and reports approved/declined status | External gateway exception, invalid amount, mismatched reference |
| Bridge | `HealthcareService` with `DeliveryMethod`; `EmailChannel`, `SmsChannel`, delivery classes | Sends consultation, test, prescription, and reminder work through interchangeable channels | Run one service with email and SMS, then online/home delivery | Service behavior stays stable while delivery implementation changes | Null channel/delivery, channel failure, unsupported delivery |
| Builder | `MedicalRecordBuilder` | Creates immutable medical records with optional fields | Build a complete record and build with missing required IDs | Complete record contains supplied values; missing required data throws `IllegalStateException` | Null date/type, mutable list leakage, blank IDs |
| Chain of Responsibility | `EmergencyRequestHandler` and reception/nurse/doctor/specialist/department handlers | Emergency intake and triage | Submit a valid emergency request through the complete chain | Each applicable handler appends a trail entry and the request reaches the department | Blank patient, broken link, handler ordering error, duplicate processing |
| Command | `BookAppointmentCommand`, `ConfirmAppointmentCommand`, `CancelAppointmentCommand`, `CompleteAppointmentCommand` | Encapsulates appointment actions for UI/workflow callers | Execute each command against a fake appointment service | Correct service method is called; booking command exposes its result | Command executed twice, null service/request, service exception |
| Composite | `HealthcareGroup`, `Hospital`, `Department`, `RecordSection` | Represents hospital, department, and record hierarchies uniformly | Add/remove nested nodes and render/inspect the tree | Parent exposes children and leaf/group operations follow the common node contract | Null child, duplicate child, removing absent child, accidental mutation of copied children |
| Decorator | `AppointmentServiceDecorator` and billing decorators such as `SpecialistConsultationDecorator` | Adds consultation options and charges | Wrap a base service with specialist, ECG, blood-test, home-visit, and emergency decorators | Description includes every option and charge is cumulative | Wrong charge order, double decoration, null delegate |
| Facade | `PatientCareFacade` | Intended consultation completion workflow | Invoke consultation completion with record and service fakes | Workflow persists record, billing/notification collaborators are invoked when implemented | Current method is a known gap/TODO; test must remain pending until behavior is implemented |
| Factory Method | `PatientFactory`, `DoctorFactory`, `NurseFactory`, `AdminFactory` | Creates role-specific users during registration | Register one valid request for each role | Correct concrete model, normalized email, hashed password, and role-specific fields are persisted | Missing role fields, duplicate email, unknown factory, invalid email/password |
| Flyweight | `MedicationCatalog`, `MedicationCatalogItem` | Reuses medication reference data in prescriptions | Request the same medication code twice with different display data | Same catalog instance is returned for the same code | Empty code, accidental cross-catalog sharing, stale metadata, unbounded catalog |
| Interpreter | `ReportQueryInterpreter` and expression classes | Filters appointment reports using a small query language | Parse `DOCTOR = D1 AND STATUS = CONFIRMED AND PRIORITY > 50` | Only appointments satisfying all terms match | Unknown field, invalid status/number, unsupported syntax, null query semantics |
| Iterator | `PatientHistoryIterator` | Merges patient appointments and medical records chronologically | Iterate mixed records/appointments with unsorted input | Entries are returned in ascending occurrence time without exposing collection internals | Null lists, unstable equal timestamps, missing dates, `next()` exhaustion |
| Mediator | `HealthcareMediatorImpl` and colleague classes | Routes care-team communication | Register patient, doctor, nurse, pharmacy, and reception participants; send a message | All participants except sender receive exactly one message | Unregistered sender, duplicate participant, null participant/message, recursive notifications |
| Memento | `MedicalRecordDraft`, `MedicalRecordMemento`, `MedicalRecordHistory` | Undo/redo medical-record drafts | Save two versions, restore, then redo | Previous version is restored and redo returns it; new save clears redo history | Restore with empty history, redo with empty redo stack, mutable snapshot leakage |
| Observer | `Appointment` observers and `PatientAppointmentObserver`, `DoctorAppointmentObserver`, `NotificationServiceAppointmentObserver` | Publishes appointment status changes | Attach observers and confirm/cancel an appointment | Each observer receives previous and new status and notification side effects occur | Duplicate observer registration, observer exception stopping others, missed previous status |
| Prototype | `MedicalRecordTemplate` and concrete consultation templates | Reuses consultation record defaults | Copy a template, mutate copied lists through builder input, compare original | Copy preserves values but has independent mutable collections | Shallow copy, null defaults, template accidentally persisted as patient record |
| Proxy | `MedicalRecordProxy` | Authorizes sensitive record access through `SessionContext` | Access as owning patient, assigned doctor, admin, unrelated user, and unauthenticated user | Owner/doctor/admin may read; unrelated and unauthenticated users receive `SecurityException` | Stale session, missing target record, incorrect role check, ID mismatch |
| Singleton | `DatabaseConnection`, `SessionContext` | Shared database/session lifecycle | Call `getInstance()` twice and update/clear session | Same instance is returned; session state is visible and clearable | Thread race, connection leak, state leaking between tests |
| State | `RequestedState`, `ConfirmedState`, `CheckedInState`, `InConsultationState`, `CompletedState`, `CancelledState`, `RejectedState` | Appointment lifecycle | Execute valid sequence requested -> confirmed -> checked in -> consultation -> completed and invalid actions | Valid actions change status; invalid actions throw the state-defined exception | Illegal transition accepted, wrong rehydrated state, observer fired on failed transition |
| Strategy | `NormalPriority`, `FollowUpPriority`, `EmergencyPriority`; payment strategies | Selects scheduling priority and payment calculation | Compare priority scores and payment strategy outputs | Strategy-specific type/score or payment behavior is selected without changing caller code | Null strategy, negative amount, wrong score ordering, unsupported method |
| Template Method | `ConsultationProcess` with general/follow-up/emergency processes | Standardizes consultation workflow with specialized steps | Run each consultation process and inspect ordered steps | Invariant workflow order is preserved and subclass steps differ appropriately | Hook skipped, wrong order, shared mutable context, incomplete specialization |
| Visitor | `BillingVisitor`, `ReportVisitor`, `AuditVisitor`, `ClinicalSummaryVisitor` | Reports, audit trails, clinical summaries, and billing | Visit patient, prescription, diagnosis, and report entities | Each visitor accumulates only its defined output and renders a stable result | Visitor state reused across patients, unknown entity, missing medication price |

## 3. Functional test scenarios

| Area | Scenario | Expected result | Failure cases |
|---|---|---|---|
| Login | Valid credentials, invalid password, unknown email, null inputs, logout | Valid login returns user and updates `SessionContext`; invalid login is empty; logout clears session | Case normalization failure, password hash mismatch, session not cleared |
| Registration | Create patient, doctor, nurse, and admin; validate required role fields | Correct factory/model is persisted with normalized email and hashed password | Duplicate email, invalid email, short password, missing role-specific fields |
| Patient creation | Register patient with patient number and retrieve by ID/email | Patient is saved and queryable | Blank number, duplicate email, repository failure |
| Doctor creation | Register doctor with license and specialty | Doctor is saved with role-specific data | Missing license/specialty, duplicate email |
| Appointment booking | Valid request for normal/follow-up/emergency priority | Appointment starts in `REQUESTED` with strategy metadata and is persisted | Null request, blank patient/doctor, null date/strategy |
| Appointment cancellation | Cancel requested and confirmed appointments | Appointment becomes `CANCELLED` and is saved | Cancelling completed/rejected appointment, unknown ID |
| Appointment state transitions | Confirm, check in, start consultation, complete; reject from requested | Only valid state transitions succeed and status observers run | Illegal transition, observer exception, unknown ID |
| Medical record creation | Build and save complete record | Immutable record is persisted with symptoms/test results copied | Missing IDs/date/type, mutable source collections |
| Medical record access authorization | Patient owner, assigned doctor, admin, unrelated user, unauthenticated access | Allowed roles retrieve record; others get `SecurityException` | Wrong user ID comparison, stale session |
| Prescription creation | Create prescription with catalog medication and retrieve by patient | Prescription and items persist; repeated catalog code reuses flyweight | Blank patient/doctor/medication, null items |
| Payment | Issue invoice for appointment; record valid payment; reject invalid payment | Invoice/payment are persisted and payment is `COMPLETED` | Unknown appointment/invoice, zero/negative amount, blank method |
| Notifications | Notify recipient and appointment status observer | Channel delivery and notification persistence both occur | Channel failure, blank recipient/message, duplicate notifications |
| Emergency handling | Register case and run full triage chain | Case persists and trail records intake, nursing, doctor/specialist/department handling | Blank patient, missing chain link, invalid severity |
| MongoDB CRUD | For each repository, save, find by ID, find all/specialized query, delete | Round trip preserves mapped fields and delete removes entity | Connection unavailable, mapper loss, duplicate IDs, cleanup failure |

## 4. Automation and execution

Automated tests should live under `src/test/java` and use JUnit 5. Prefer in-memory repository doubles for service tests. Do not instantiate `AppContext` in unit tests because it creates Mongo repositories and a database connection.

Commands:

```text
mvn clean test
mvn "-Dmongo.integration=true" "-Dtest=*Mongo*" test
```

The Mongo integration tests must use an isolated database/collection namespace and delete all created documents in `@AfterEach`. A failed connection should be reported as an environment/setup failure, not silently treated as a passing CRUD test. The current repository does not yet contain a `*Mongo*` JUnit class; add or enable that integration class before running the second command.

## 5. Exit criteria

- Every row in the GoF matrix has at least one automated test or an explicit pending/gap test.
- Core functional scenarios pass with deterministic test doubles.
- Authorization denies unauthenticated and unrelated access.
- Invalid inputs produce the documented exception/result.
- Mongo CRUD passes against a clean test database.
- No production architecture changes are introduced solely for testability.
- Known gap: `PatientCareFacade.completeConsultation` is currently a TODO and requires a product decision before its test can pass.
