package rmit.saintgiong.jmnotificationservice.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rmit.saintgiong.jmnotificationapi.internal.common.dto.response.NotificationResponseDto;
import rmit.saintgiong.jmnotificationapi.internal.services.InternalGetNotificationInterface;
import rmit.saintgiong.jmnotificationservice.domain.entity.CompanyNotificationEntity;
import rmit.saintgiong.jmnotificationservice.domain.mapper.NotificationMapper;
import rmit.saintgiong.jmnotificationservice.domain.repository.CompanyNotificationRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetNotificationService implements InternalGetNotificationInterface {

    private final CompanyNotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public NotificationResponseDto getNotificationById(UUID id) {
        CompanyNotificationEntity entity = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        return notificationMapper.toResponse(entity);
    }

    @Override
    public List<NotificationResponseDto> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<NotificationResponseDto> getNotificationsByCompanyId(UUID companyId, Pageable pageable) {
        return notificationRepository.findByCompanyId(companyId, pageable)
                .map(notificationMapper::toResponse);
    }
}
