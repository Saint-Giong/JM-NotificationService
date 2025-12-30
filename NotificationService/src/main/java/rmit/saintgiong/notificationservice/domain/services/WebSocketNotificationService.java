package rmit.saintgiong.notificationservice.domain.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import rmit.saintgiong.notificationapi.common.dto.response.NotificationResponse;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketNotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    // SimpUserRegistry is used to retrieve information about connected users
    private final SimpUserRegistry userRegistry;

    public void sendNotification(UUID companyId, NotificationResponse notification) {
        log.info("Sending WebSocket notification to company: {}", companyId);
        messagingTemplate.convertAndSend("/topic/company/" + companyId, notification);
    }

    public boolean isCompanyConnected(UUID companyId) {
        // This assumes the WebSocket connection is authenticated and the Principal name is the companyId
        // If unauthenticated or using a different principal naming strategy, this needs adjustment.
        // For simplicity/demonstration in this context, we try to find a user by name.
        SimpUser user = userRegistry.getUser(companyId.toString());
        if (user != null && user.hasSessions()) {
             log.info("Company {} is connected via WebSocket.", companyId);
             return true;
        }
        
        // Fallback/Warning: If users are anonymous, this registry check won't work as expected for specific IDs.
        // But per requirement, we implement the check.
        return false;
    }
}
