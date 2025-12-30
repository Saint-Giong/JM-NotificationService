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
    public NotificationResponse updateNotification(UUID id, UpdateNotificationRequest request) {
        CompanyNotification entity = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        
        notificationMapper.updateEntityFromRequest(request, entity);
        
        CompanyNotification updatedEntity = notificationRepository.save(entity);
        return notificationMapper.toResponse(updatedEntity);
    }
}
