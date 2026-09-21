package com.healthsync.flyweight;
import java.util.HashMap;
import java.util.Map;
/** Caches reusable medication reference objects. */
public class MedicationCatalog {
    private final Map<String, MedicationCatalogItem> items = new HashMap<>();
    public MedicationCatalogItem get(String code, String name, String strength) { return items.computeIfAbsent(code, ignored -> new MedicationCatalogItem(code, name, strength)); }
    // TODO: Load reference entries from MongoDB.
}
