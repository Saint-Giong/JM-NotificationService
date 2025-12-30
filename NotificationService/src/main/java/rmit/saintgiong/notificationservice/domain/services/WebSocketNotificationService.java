package rmit.saintgiong.notificationservice.domain.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;
import rmit.saintgiong.notificationapi.common.dto.response.NotificationResponse;

import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketNotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final Map<UUID, CompletableFuture<Boolean>> pendingAcks = new ConcurrentHashMap<>();

    public void sendNotification(UUID companyId, NotificationResponse notification) {
        log.info("Sending WebSocket notification to company: {}", companyId);
        
        UUID notificationId = notification.getNotificationId();
        CompletableFuture<Boolean> ackFuture = new CompletableFuture<>();
        pendingAcks.put(notificationId, ackFuture);

        try {
            messagingTemplate.convertAndSend("/topic/company/" + companyId, notification);
            
            // Wait for acknowledgement (e.g., 5 seconds)
            ackFuture.get(5, TimeUnit.SECONDS);
            log.info("Received acknowledgement for notification: {}", notificationId);
        } catch (TimeoutException e) {
            log.error("Timed out waiting for acknowledgement for notification: {}", notificationId);
            throw new RuntimeException("Failed to receive WebSocket acknowledgement for notification: " + notificationId, e);
        } catch (Exception e) {
            log.error("Error sending WebSocket notification", e);
            throw new RuntimeException("Error sending WebSocket notification", e);
        } finally {
            pendingAcks.remove(notificationId);
        }
    }

    @MessageMapping("/ack")
    public void handleAck(@Payload String notificationIdStr) {
        try {
            // Remove potential double quotes if sent as JSON string
            String cleanId = notificationIdStr.replace("\"", "");
            UUID notificationId = UUID.fromString(cleanId);
            log.info("Received ack for notificationId: {}", notificationId);
            
            CompletableFuture<Boolean> future = pendingAcks.get(notificationId);
            if (future != null) {
                future.complete(true);
            }
        } catch (Exception e) {
            log.warn("Invalid ack payload: {}", notificationIdStr);
        }
    }
}
