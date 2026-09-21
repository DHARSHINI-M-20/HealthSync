package com.healthsync.interpreter;
/** Parses a deliberately small rule language: AGE/BP, >, and AND. */
public class HealthcareRuleInterpreter {
    public HealthRuleExpression parse(String expression) {
        String[] conditions=expression.toUpperCase().trim().split("\\s+AND\\s+");
        HealthRuleExpression result=context -> true;
        for (String condition : conditions) { HealthRuleExpression next=parseCondition(condition.trim()); HealthRuleExpression previous=result; result=context -> previous.evaluate(context) && next.evaluate(context); }
        return result;
    }
    private HealthRuleExpression parseCondition(String condition) {
        String[] parts=condition.split("\\s+>");
        if (parts.length != 2) throw new IllegalArgumentException("Only FIELD > number conditions are supported");
        int threshold=Integer.parseInt(parts[1].trim());
        return switch (parts[0].trim()) { case "AGE" -> context -> context.age() > threshold; case "BP" -> context -> context.bloodPressure() > threshold; default -> throw new IllegalArgumentException("Supported fields are AGE and BP"); };
    }
}
