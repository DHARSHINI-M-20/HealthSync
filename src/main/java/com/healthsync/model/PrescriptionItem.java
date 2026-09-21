package com.healthsync.model;
import com.healthsync.flyweight.MedicationCatalogItem;
/** One prescribed medication and instruction. */
public record PrescriptionItem(MedicationCatalogItem medication, String dosage, String instructions) { }
