package rmit.saintgiong.notificationapi.services;

import rmit.saintgiong.notificationapi.common.dto.request.UpdateNotificationRequest;
import rmit.saintgiong.notificationapi.common.dto.response.NotificationResponse;
import java.util.UUID;

public interface InternalUpdateNotificationInterface {
    NotificationResponse updateNotification(UUID id, UpdateNotificationRequest request);
}
