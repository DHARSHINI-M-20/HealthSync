package com.healthsync.composite;
/** Leaf service in a hospital department tree. */
public record HealthcareServiceNode(String name) implements HealthcareNode { }
