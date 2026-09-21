package com.healthsync.prototype;
/** Cloneable saved report configuration. */
public class ReportTemplate implements Cloneable {
    private String name; private String query;
    public ReportTemplate(String name, String query) { this.name=name; this.query=query; }
    public ReportTemplate copy() { try { return (ReportTemplate) clone(); } catch (CloneNotSupportedException ex) { throw new AssertionError(ex); } }
    // TODO: Add selected columns, formatting, and ownership metadata.
}
