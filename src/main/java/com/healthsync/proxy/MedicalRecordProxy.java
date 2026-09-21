package com.healthsync.proxy;
import com.healthsync.model.MedicalRecord;
import com.healthsync.model.Role;
import com.healthsync.model.User;
import com.healthsync.util.SessionContext;
import java.util.Optional;
/** Authorization proxy for sensitive clinical data. */
public class MedicalRecordProxy implements MedicalRecordAccess {
    private final MedicalRecordAccess target;
    public MedicalRecordProxy(MedicalRecordAccess target) { this.target=target; }
    public Optional<MedicalRecord> findById(String recordId) {
        User user=SessionContext.getInstance().getCurrentUser().orElseThrow(() -> new SecurityException("Authentication is required to access medical records"));
        Optional<MedicalRecord> record=target.findById(recordId);
        record.ifPresent(value -> authorize(user, value));
        return record;
    }
    private void authorize(User user, MedicalRecord record) {
        boolean allowed=user.getRole() == Role.ADMIN || (user.getRole() == Role.PATIENT && user.getId().equals(record.getPatientId())) || (user.getRole() == Role.DOCTOR && user.getId().equals(record.getDoctorId()));
        if (!allowed) throw new SecurityException("You are not authorized to access this medical record");
    }
}
