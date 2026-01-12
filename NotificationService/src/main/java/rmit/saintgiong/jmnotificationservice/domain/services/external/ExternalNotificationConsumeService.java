package rmit.saintgiong.jmnotificationservice.domain.services.external;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import rmit.saintgiong.jmnotificationapi.external.common.dto.avro.ApplicantNotificationAction;
import rmit.saintgiong.jmnotificationapi.external.common.dto.avro.SubscriptionExpiryNotificationRecord;
import rmit.saintgiong.jmnotificationapi.external.services.ExternalNotificationConsumeInterface;
import rmit.saintgiong.jmnotificationapi.internal.common.dto.request.NotificationBuilderDto;
import rmit.saintgiong.jmnotificationapi.internal.common.dto.response.NotificationResponseDto;
import rmit.saintgiong.jmnotificationapi.internal.common.dto.response.NotificationResponseMessageDto;
import rmit.saintgiong.jmnotificationapi.internal.common.type.SubscriptionKafkaTopic;
import rmit.saintgiong.jmnotificationapi.internal.services.InternalCreateNotificationInterface;
import rmit.saintgiong.jmnotificationapi.internal.services.InternalUpdateNotificationInterface;
import rmit.saintgiong.jmnotificationservice.domain.services.websocket.WebSocketNotificationService;
import rmit.saintgiong.shared.type.KafkaTopic;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExternalNotificationConsumeService implements ExternalNotificationConsumeInterface {

    private final InternalCreateNotificationInterface internalCreateNotificationService;
    private final InternalUpdateNotificationInterface internalUpdateNotificationService;
    private final WebSocketNotificationService webSocketService;

    @KafkaListener(topics = KafkaTopic.NEW_APPLICANT_TOPIC_REQUEST)
    @SendTo(KafkaTopic.NEW_APPLICANT_TOPIC_REPLIED)
    public NotificationResponseMessageDto handleNewCreatedApplicationRequestSentFromJA(ApplicantNotificationAction message) {
        log.info("Received new applicant notification for company: {}", message.getCompanyId());

        NotificationResponseDto response = internalCreateNotificationService.createNotification(
                NotificationBuilderDto.builder()
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
//            internalUpdateNotificationService.updateNotificationIsRead(response.getNotificationId());

        } catch (Exception e) {
            log.error("Failed to send WebSocket notification", e);

            return NotificationResponseMessageDto.builder()
                    .notification("Create and Store new notification successfully")
                    .websocket("Company can not receive notification through Websocket")
                    .build();
        }

        return NotificationResponseMessageDto.builder()
                .notification("Create and Store new notification successfully")
                .websocket("Company received notification through Websocket")
                .build();
    }

    @KafkaListener(topics = KafkaTopic.EDIT_APPLICANT_TOPIC_REQUEST)
    @SendTo(KafkaTopic.EDIT_APPLICANT_TOPIC_REPLIED)
    public NotificationResponseMessageDto handleNewEditedApplicationRequestSentFromJA(ApplicantNotificationAction message) {
        log.info("Received edit applicant notification for company: {}", message.getCompanyId());

        NotificationResponseDto response = internalCreateNotificationService.createNotification(
                NotificationBuilderDto.builder()
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
//            internalUpdateNotificationService.updateNotificationIsRead(response.getNotificationId());

        } catch (Exception e) {
            log.error("Failed to send WebSocket notification", e);
            return NotificationResponseMessageDto.builder()
                    .notification("Create and Store new notification successfully")
                    .websocket("Company can not receive notification through Websocket")
                    .build();
        }

        return NotificationResponseMessageDto.builder()
                .notification("Create and Store new notification successfully")
                .websocket("Company received notification through Websocket")
                .build();
    }

    @KafkaListener(topics = SubscriptionKafkaTopic.SUBSCRIPTION_EXPIRY_NOTIFICATION_TOPIC)
    @SendTo(SubscriptionKafkaTopic.SUBSCRIPTION_EXPIRY_NOTIFICATION_TOPIC)
    public NotificationResponseMessageDto handleExpiryNotificationSentFromSubscription(SubscriptionExpiryNotificationRecord message) {
        log.info("Received subscription expiry notification for company: {}", message.getCompanyId());

        NotificationResponseDto response = internalCreateNotificationService.createNotification(
                NotificationBuilderDto.builder()
                        .companyId(message.getCompanyId())
                        .applicantId(null)
                        .title(message.getTitle())
                        .message(message.getMessage())
                        .isRead(false)
                        .build()
        );

        try {
            webSocketService.sendNotification(message.getCompanyId(), response);
            // If WebSocket send is successful, mark as read
//            internalUpdateNotificationService.updateNotificationIsRead(response.getNotificationId());

        } catch (Exception e) {
            log.error("Failed to send WebSocket notification for subscription expiry", e);
            return NotificationResponseMessageDto.builder()
                    .notification("Subscription Expiry Notification Created")
                    .websocket("Company can not receive notification through Websocket")
                    .build();
        }

        return NotificationResponseMessageDto.builder()
                .notification("Subscription Expiry Notification Created")
                .websocket("Company can not receive notification through Websocket")
                .build();
    }
}
