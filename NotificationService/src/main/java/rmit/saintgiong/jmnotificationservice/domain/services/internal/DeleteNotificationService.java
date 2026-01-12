package rmit.saintgiong.jmnotificationservice.domain.services.internal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import rmit.saintgiong.jmnotificationapi.internal.services.InternalDeleteNotificationInterface;
import rmit.saintgiong.jmnotificationservice.domain.repository.CompanyNotificationRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteNotificationService implements InternalDeleteNotificationInterface {

    private final CompanyNotificationRepository notificationRepository;

    @Override
    public void deleteNotification(UUID id) {
        if (!notificationRepository.existsById(id)) {
            throw new RuntimeException("Notification not found with id: " + id);
        }
        notificationRepository.deleteById(id);
    }
}
