package com.healthsync.strategy;
/** Priority used for planned follow-up visits. */
public class FollowUpPriority implements AppointmentPriorityStrategy { public String type() { return "FOLLOW_UP"; } public int priorityScore() { return 10; } }
