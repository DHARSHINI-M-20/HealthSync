package com.healthsync.flyweight;
import java.util.HashMap;
import java.util.Map;
/** Shares immutable medical reference data instead of duplicating it in records and profiles. */
public class MedicalInformationFactory {
    private final Map<String, MedicineType> medicines=new HashMap<>();
    private final Map<String, MedicalTestType> tests=new HashMap<>();
    private final Map<String, DoctorSpecialization> specializations=new HashMap<>();
    public MedicineType medicineType(String code, String name, String category) { return medicines.computeIfAbsent(code, ignored -> new MedicineType(code, name, category)); }
    public MedicalTestType medicalTestType(String code, String name, String method) { return tests.computeIfAbsent(code, ignored -> new MedicalTestType(code, name, method)); }
    public DoctorSpecialization doctorSpecialization(String code, String name) { return specializations.computeIfAbsent(code, ignored -> new DoctorSpecialization(code, name)); }
}
