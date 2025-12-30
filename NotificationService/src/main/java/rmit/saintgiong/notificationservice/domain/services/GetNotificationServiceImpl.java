package rmit.saintgiong.notificationservice.domain.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rmit.saintgiong.notificationapi.common.dto.response.NotificationResponse;
import rmit.saintgiong.notificationapi.services.InternalGetNotificationInterface;
import rmit.saintgiong.notificationservice.domain.entity.CompanyNotification;
import rmit.saintgiong.notificationservice.domain.mapper.NotificationMapper;
import rmit.saintgiong.notificationservice.domain.repository.CompanyNotificationRepository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetNotificationServiceImpl implements InternalGetNotificationInterface {

    private final CompanyNotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    @Override
    public NotificationResponse getNotificationById(UUID id) {
        CompanyNotification entity = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        return notificationMapper.toResponse(entity);
    }

    @Override
    public List<NotificationResponse> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(notificationMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Page<NotificationResponse> getNotificationsByCompanyId(UUID companyId, Pageable pageable) {
        return notificationRepository.findByCompanyId(companyId, pageable)
                .map(notificationMapper::toResponse);
    }
}
