package com.healthsync.composite;
import java.util.List;
/** Component for the hospital organization/service tree. */
public interface HealthcareNode {
    String name();
    default void add(HealthcareNode child) { throw new UnsupportedOperationException("Leaf nodes cannot have children"); }
    default void remove(HealthcareNode child) { throw new UnsupportedOperationException("Leaf nodes cannot have children"); }
    default List<HealthcareNode> children() { return List.of(); }
}
