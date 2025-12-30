package rmit.saintgiong.notificationapi.services;

import rmit.saintgiong.notificationapi.common.dto.response.NotificationResponseDto;
import java.util.UUID;

public interface InternalUpdateNotificationInterface {
    public NotificationResponseDto updateNotificationIsRead(UUID id);
}
