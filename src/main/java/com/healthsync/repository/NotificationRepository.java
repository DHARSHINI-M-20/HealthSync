package com.healthsync.repository;
import com.healthsync.model.Notification;
import java.util.List;
/** Persistence boundary for notification delivery history. */
public interface NotificationRepository extends Repository<Notification> { List<Notification> findByRecipientId(String recipientId); }
