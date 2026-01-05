package rmit.saintgiong.jmnotificationservice.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rmit.saintgiong.jmnotificationapi.internal.common.dto.request.NotificationDto;
import rmit.saintgiong.jmnotificationapi.internal.common.dto.response.NotificationResponseDto;
import rmit.saintgiong.jmnotificationapi.internal.services.InternalCreateNotificationInterface;
import rmit.saintgiong.jmnotificationservice.domain.entity.CompanyNotificationEntity;
import rmit.saintgiong.jmnotificationservice.domain.mapper.NotificationMapper;
import rmit.saintgiong.jmnotificationservice.domain.repository.CompanyNotificationRepository;

@Service
@RequiredArgsConstructor
public class CreateNotificationService implements InternalCreateNotificationInterface {

    private final CompanyNotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public NotificationResponseDto createNotification(NotificationDto request) {
        CompanyNotificationEntity entity = notificationMapper.toEntity(request);

        CompanyNotificationEntity savedEntity = notificationRepository.save(entity);
        return notificationMapper.toResponse(savedEntity);
    }
}
