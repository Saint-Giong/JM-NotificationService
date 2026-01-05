package rmit.saintgiong.jmnotificationapi.internal.services;

import rmit.saintgiong.jmnotificationapi.internal.common.dto.response.NotificationResponseDto;
import java.util.UUID;

public interface InternalUpdateNotificationInterface {
    public NotificationResponseDto updateNotificationIsRead(UUID id);
}
