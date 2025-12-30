package rmit.saintgiong.notificationservice.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rmit.saintgiong.notificationapi.common.dto.request.UpdateNotificationRequest;
import rmit.saintgiong.notificationapi.common.dto.response.NotificationResponse;
import rmit.saintgiong.notificationapi.services.InternalUpdateNotificationInterface;
import rmit.saintgiong.notificationservice.domain.entity.CompanyNotification;
import rmit.saintgiong.notificationservice.domain.mapper.NotificationMapper;
import rmit.saintgiong.notificationservice.domain.repository.CompanyNotificationRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateNotificationService implements InternalUpdateNotificationInterface {

    private final CompanyNotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public NotificationResponse updateNotificationIsRead(UUID id) {
        CompanyNotification entity = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        
        entity.setRead(true);

        CompanyNotification updatedEntity = notificationRepository.save(entity);
        return notificationMapper.toResponse(updatedEntity);
    }
}
