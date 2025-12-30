package rmit.saintgiong.notificationservice.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rmit.saintgiong.notificationapi.common.dto.request.NotificationDto;
import rmit.saintgiong.notificationapi.common.dto.response.NotificationResponseDto;
import rmit.saintgiong.notificationapi.services.InternalCreateNotificationInterface;
import rmit.saintgiong.notificationservice.domain.entity.CompanyNotificationEntity;
import rmit.saintgiong.notificationservice.domain.mapper.NotificationMapper;
import rmit.saintgiong.notificationservice.domain.repository.CompanyNotificationRepository;

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
