package com.healthsync.strategy;
/** Highest priority for urgent/emergency scheduling. */
public class EmergencyPriority implements AppointmentPriorityStrategy { public String type() { return "EMERGENCY"; } public int priorityScore() { return 100; } }
