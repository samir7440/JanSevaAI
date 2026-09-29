package com.janseva.backend.repository;

import com.janseva.backend.model.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {

    // User notifications
    List<Notification> findByUserIdOrderByTimestampDesc(String userId);

    // Unread notifications
    List<Notification> findByUserIdAndReadFalse(String userId);
}