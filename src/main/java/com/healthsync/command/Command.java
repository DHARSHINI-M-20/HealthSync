package com.healthsync.command;
/** Executable action suitable for Swing controls and audit logging. */
public interface Command { void execute(); default void undo() { /* TODO: Provide undo where business rules permit. */ } }
