package rmit.saintgiong.notificationapi.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rmit.saintgiong.notificationapi.common.dto.response.NotificationResponse;

import java.util.List;
import java.util.UUID;

public interface InternalGetNotificationInterface {
    NotificationResponse getNotificationById(UUID id);
    List<NotificationResponse> getAllNotifications();
    Page<NotificationResponse> getNotificationsByCompanyId(UUID companyId, Pageable pageable);
}
