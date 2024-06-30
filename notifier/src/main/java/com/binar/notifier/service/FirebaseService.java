package com.binar.notifier.service;

import com.binar.notifier.stream.data.firebase.SendNotificationDto;

public interface FirebaseService {
    void sendNotification(SendNotificationDto sendNotification);
}
