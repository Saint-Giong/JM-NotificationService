package rmit.saintgiong.notificationservice.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rmit.saintgiong.notificationapi.common.dto.request.CreateNotificationRequest;
import rmit.saintgiong.notificationapi.common.dto.response.NotificationResponse;
import rmit.saintgiong.notificationapi.services.InternalCreateNotificationInterface;
import rmit.saintgiong.notificationservice.domain.entity.CompanyNotification;
import rmit.saintgiong.notificationservice.domain.mapper.NotificationMapper;
import rmit.saintgiong.notificationservice.domain.repository.CompanyNotificationRepository;

@Service
@RequiredArgsConstructor
public class CreateNotificationService implements InternalCreateNotificationInterface {

    private final CompanyNotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public NotificationResponse createNotification(CreateNotificationRequest request) {
        CompanyNotification entity = notificationMapper.toEntity(request);

        CompanyNotification savedEntity = notificationRepository.save(entity);
        return notificationMapper.toResponse(savedEntity);
    }
}
