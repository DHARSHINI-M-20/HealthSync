package com.healthsync.template;
public class GeneralConsultationProcess extends ConsultationProcess {
    protected void examination(ConsultationContext context) { context.addStep("General physical examination"); }
    protected void diagnosis(ConsultationContext context) { context.addStep("General diagnosis recorded"); }
    protected void prescription(ConsultationContext context) { context.addStep("Standard prescription issued"); }
}
