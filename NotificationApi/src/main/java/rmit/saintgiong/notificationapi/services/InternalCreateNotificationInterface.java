package rmit.saintgiong.notificationapi.services;

import rmit.saintgiong.notificationapi.common.dto.request.CreateNotificationRequest;
import rmit.saintgiong.notificationapi.common.dto.response.NotificationResponse;

public interface InternalCreateNotificationInterface {
    NotificationResponse createNotification(CreateNotificationRequest request);
}
