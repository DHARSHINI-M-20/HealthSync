package com.healthsync.template;
public class FollowUpConsultationProcess extends ConsultationProcess {
    protected void examination(ConsultationContext context) { context.addStep("Treatment-progress examination"); }
    protected void diagnosis(ConsultationContext context) { context.addStep("Previous diagnosis reviewed"); }
    protected void prescription(ConsultationContext context) { context.addStep("Prescription adjusted if needed"); }
}
