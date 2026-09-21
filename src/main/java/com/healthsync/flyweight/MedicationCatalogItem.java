package com.healthsync.flyweight;
/** Immutable shared medication metadata. */
public record MedicationCatalogItem(String code, String name, String strength) { }
