package com.healthsync.template;
public class EmergencyConsultationProcess extends ConsultationProcess {
    protected void examination(ConsultationContext context) { context.addStep("Emergency stabilization and examination"); }
    protected void diagnosis(ConsultationContext context) { context.addStep("Urgent diagnosis recorded"); }
    protected void prescription(ConsultationContext context) { context.addStep("Emergency medication/treatment issued"); }
}
