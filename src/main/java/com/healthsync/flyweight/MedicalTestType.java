package com.healthsync.flyweight;
/** Immutable shared medical-test classification. */
public record MedicalTestType(String code, String name, String specimenOrMethod) { }
