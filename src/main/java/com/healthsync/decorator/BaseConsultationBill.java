package com.healthsync.decorator;
import java.math.BigDecimal;
public record BaseConsultationBill(BigDecimal amount) implements BillComponent { public String description() { return "Consultation fee"; } }
