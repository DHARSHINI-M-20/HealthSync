package com.healthsync.strategy;
/** Varies scheduling priority without changing appointment service code. */
public interface AppointmentPriorityStrategy { String type(); int priorityScore(); }
