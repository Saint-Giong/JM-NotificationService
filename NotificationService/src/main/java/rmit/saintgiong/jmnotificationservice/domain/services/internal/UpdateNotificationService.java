package rmit.saintgiong.jmnotificationservice.domain.services.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rmit.saintgiong.jmnotificationapi.internal.common.dto.response.NotificationResponseDto;
import rmit.saintgiong.jmnotificationapi.internal.services.InternalUpdateNotificationInterface;
import rmit.saintgiong.jmnotificationservice.domain.entity.CompanyNotificationEntity;
import rmit.saintgiong.jmnotificationservice.domain.mapper.NotificationMapper;
import rmit.saintgiong.jmnotificationservice.domain.repository.CompanyNotificationRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateNotificationService implements InternalUpdateNotificationInterface {

    private final CompanyNotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public NotificationResponseDto updateNotificationIsRead(UUID id) {
        CompanyNotificationEntity entity = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        
        entity.setIsRead(true);

        CompanyNotificationEntity updatedEntity = notificationRepository.save(entity);
        return notificationMapper.toResponse(updatedEntity);
    }
}
