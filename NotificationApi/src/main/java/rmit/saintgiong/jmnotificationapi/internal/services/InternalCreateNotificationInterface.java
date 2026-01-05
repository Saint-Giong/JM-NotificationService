package rmit.saintgiong.jmnotificationapi.internal.services;

import rmit.saintgiong.jmnotificationapi.internal.common.dto.request.NotificationDto;
import rmit.saintgiong.jmnotificationapi.internal.common.dto.response.NotificationResponseDto;

public interface InternalCreateNotificationInterface {
    NotificationResponseDto createNotification(NotificationDto request);
}
