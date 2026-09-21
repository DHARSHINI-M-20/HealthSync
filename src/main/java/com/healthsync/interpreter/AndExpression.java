package com.healthsync.interpreter;

import com.healthsync.model.Appointment;

/** Conjunction of two report expressions. */
public class AndExpression implements ReportExpression {
    private final ReportExpression left;
    private final ReportExpression right;
    public AndExpression(ReportExpression left, ReportExpression right) { this.left = left; this.right = right; }
    @Override public boolean interpret(Appointment appointment) { return left.interpret(appointment) && right.interpret(appointment); }
}