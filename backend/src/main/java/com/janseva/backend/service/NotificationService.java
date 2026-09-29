package com.janseva.backend.service;

import com.janseva.backend.model.Notification;
import com.janseva.backend.repository.NotificationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {


@Autowired
private NotificationRepository repository;

/*
 * ===============================
 * SEND NOTIFICATION
 * ===============================
 */

public void sendNotification(

        String userId,

        String title,

        String message

) {

    Notification notification =
            new Notification();

    notification.setUserId(
            userId
    );

    notification.setTitle(
            title
    );

    notification.setMessage(
            message
    );

    notification.setRead(false);

    notification.setTimestamp(
            LocalDateTime.now()
    );

    repository.save(
            notification
    );

    System.out.println(

            "NOTIFICATION SENT -> "

                    + title
    );
}

/*
 * ===============================
 * USER NOTIFICATIONS
 * ===============================
 */

public List<Notification>
getUserNotifications(
        String userId
) {

    return repository
            .findByUserIdOrderByTimestampDesc(
                    userId
            );
}

/*
 * ===============================
 * MARK AS READ
 * ===============================
 */

public void markAsRead(
        String notificationId
) {

    repository.findById(
            notificationId
    ).ifPresent(notification -> {

        notification.setRead(true);

        repository.save(notification);

    });
}


}
