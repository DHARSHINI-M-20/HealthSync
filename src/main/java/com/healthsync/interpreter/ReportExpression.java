package com.healthsync.interpreter;

import com.healthsync.model.Appointment;

/** Equality and range expressions over appointment fields, combined with AND/OR. */
public interface ReportExpression {
    boolean interpret(Appointment appointment);
}