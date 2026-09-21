package com.healthsync.template;
/** Defines stable report-generation steps while allowing report-specific data extraction. */
public abstract class ReportGenerator {
    public final String generate(String criteria) { validate(criteria); Object data=collectData(criteria); return format(data); }
    protected void validate(String criteria) { /* TODO: Validate report criteria. */ }
    protected abstract Object collectData(String criteria);
    protected abstract String format(Object data);
}
