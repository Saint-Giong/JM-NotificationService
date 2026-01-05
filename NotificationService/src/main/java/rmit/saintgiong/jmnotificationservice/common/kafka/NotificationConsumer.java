package rmit.saintgiong.jmnotificationservice.common.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import rmit.saintgiong.jmnotificationapi.external.common.dto.avro.ApplicantNotificationAction;
import rmit.saintgiong.jmnotificationapi.internal.common.dto.request.NotificationDto;
import rmit.saintgiong.jmnotificationapi.internal.services.InternalCreateNotificationInterface;
import rmit.saintgiong.jmnotificationapi.internal.services.InternalUpdateNotificationInterface;
import rmit.saintgiong.jmnotificationservice.domain.services.websocket.WebSocketNotificationService;
import rmit.saintgiong.shared.type.KafkaTopic;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final InternalCreateNotificationInterface createNotificationService;
    private final InternalUpdateNotificationInterface updateNotificationService;
    private final WebSocketNotificationService webSocketService;

    @KafkaListener(topics = KafkaTopic.NEW_APPLICANT_TOPIC_REQUEST, containerFactory = "notificationKafkaListenerContainerFactory")
    @SendTo(KafkaTopic.NEW_APPLICANT_TOPIC_REPLIED)
    public String consumeNewApplicant(ApplicantNotificationAction message) {
        log.info("Received new applicant notification for company: {}", message.getCompanyId());
        
        var response = createNotificationService.createNotification(
                NotificationDto.builder()
                        .companyId(message.getCompanyId())
                        .applicantId(message.getApplicantId())
                        .title("New Applicant")
                        .message("New Applicant ID: " + message.getApplicantId())
                        .isRead(false)
                        .build()
        );

        try {
            webSocketService.sendNotification(message.getCompanyId(), response);
            // If WebSocket send is successful, mark as read
            updateNotificationService.updateNotificationIsRead(response.getNotificationId());
        } catch (Exception e) {
            log.error("Failed to send WebSocket notification", e);
        }
        
        return "Notification Created";
    }

    @KafkaListener(topics = KafkaTopic.EDIT_APPLICANT_TOPIC_REQUEST, containerFactory = "notificationKafkaListenerContainerFactory")
    @SendTo(KafkaTopic.EDIT_APPLICANT_TOPIC_REPLIED)
    public String consumeEditApplicant(ApplicantNotificationAction message) {
        log.info("Received edit applicant notification for company: {}", message.getCompanyId());

        var response = createNotificationService.createNotification(
                NotificationDto.builder()
                        .companyId(message.getCompanyId())
                        .applicantId(message.getApplicantId())
                        .title("Applicant Updated")
                        .message("Applicant (ID: " + message.getApplicantId() + ") has updated their application.")
                        .isRead(false)
                        .build()
        );

        try {
            webSocketService.sendNotification(message.getCompanyId(), response);
            // If WebSocket send is successful, mark as read
            updateNotificationService.updateNotificationIsRead(response.getNotificationId());
        } catch (Exception e) {
            log.error("Failed to send WebSocket notification", e);
        }

        return "Notification Created";
    }
}
