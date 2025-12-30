package rmit.saintgiong.notificationapi.services;

import rmit.saintgiong.notificationapi.common.dto.request.NotificationDto;
import rmit.saintgiong.notificationapi.common.dto.response.NotificationResponseDto;

public interface InternalCreateNotificationInterface {
    NotificationResponseDto createNotification(NotificationDto request);
}
