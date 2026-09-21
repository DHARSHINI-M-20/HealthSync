package com.healthsync.strategy;
/** Standard scheduled-care priority. */
public class NormalPriority implements AppointmentPriorityStrategy { public String type() { return "NORMAL"; } public int priorityScore() { return 1; } }
