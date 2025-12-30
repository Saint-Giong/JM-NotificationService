package rmit.saintgiong.notificationapi.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rmit.saintgiong.notificationapi.common.dto.response.NotificationResponseDto;

import java.util.List;
import java.util.UUID;

public interface InternalGetNotificationInterface {
    NotificationResponseDto getNotificationById(UUID id);
    List<NotificationResponseDto> getAllNotifications();
    Page<NotificationResponseDto> getNotificationsByCompanyId(UUID companyId, Pageable pageable);
}
