package rmit.saintgiong.jmnotificationservice.domain.services.external;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import rmit.saintgiong.jmnotificationapi.external.services.ExternalNotificationConsumeInterface;
import rmit.saintgiong.jmnotificationapi.internal.common.dto.request.NotificationBuilderDto;
import rmit.saintgiong.jmnotificationapi.internal.common.dto.response.NotificationResponseDto;
import rmit.saintgiong.jmnotificationapi.internal.common.dto.response.NotificationResponseMessageDto;
import rmit.saintgiong.jmnotificationapi.internal.services.InternalCreateNotificationInterface;
import rmit.saintgiong.jmnotificationservice.domain.services.websocket.WebSocketNotificationService;
import rmit.saintgiong.shared.dto.avro.notification.ApplicantMatchNotificationRecord;
import rmit.saintgiong.shared.dto.avro.subscription.SubscriptionExpiredNotificationRecord;
import rmit.saintgiong.shared.type.KafkaTopic;

@Service
@Slf4j
@RequiredArgsConstructor
public class ExternalNotificationConsumeService implements ExternalNotificationConsumeInterface {

    private final InternalCreateNotificationInterface internalCreateNotificationService;
    private final WebSocketNotificationService webSocketService;

    @KafkaListener(topics = KafkaTopic.JM_NEW_APPLICANT_REQUEST_TOPIC)
    @SendTo(KafkaTopic.JM_NEW_APPLICANT_RESPONSE_TOPIC)
    public NotificationResponseMessageDto handleNewCreatedApplicationRequestSentFromJA(ApplicantMatchNotificationRecord message) {
        log.info("Received new applicant notification for company: {}", message.getCompanyId());

        NotificationResponseDto response = internalCreateNotificationService.createNotification(
                NotificationBuilderDto.builder()
                        .companyId(message.getCompanyId())
                        .applicantId(message.getApplicantId())
                        .title("New Applicant Match")
                        .message("New Applicant Match found! Applicant ID: " + message.getApplicantId())
                        .isRead(false)
                        .build()
        );

        try {
            webSocketService.sendNotification(message.getCompanyId(), response);
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

    @KafkaListener(topics = KafkaTopic.JM_UPDATE_APPLICANT_REQUEST_TOPIC)
    @SendTo(KafkaTopic.JM_UPDATE_APPLICANT_RESPONSE_TOPIC)
    public NotificationResponseMessageDto handleNewEditedApplicationRequestSentFromJA(ApplicantMatchNotificationRecord message) {
        log.info("Received edit applicant notification for company: {}", message.getCompanyId());

        NotificationResponseDto response = internalCreateNotificationService.createNotification(
                NotificationBuilderDto.builder()
                        .companyId(message.getCompanyId())
                        .applicantId(message.getApplicantId())
                        .title("Applicant Updated Match")
                        .message("Applicant (ID: " + message.getApplicantId() + ") has updated their profile and still matches your search criteria.")
                        .isRead(false)
                        .build()
        );

        try {
            webSocketService.sendNotification(message.getCompanyId(), response);
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

    @KafkaListener(topics = KafkaTopic.JM_SUBSCRIPTION_EXPIRED_NOTIFICATION_TOPIC)
    public NotificationResponseMessageDto handleExpiryNotificationSentFromSubscription(SubscriptionExpiredNotificationRecord message) {
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
        } catch (Exception e) {
            log.error("Failed to send WebSocket notification for subscription expiry", e);
            return NotificationResponseMessageDto.builder()
                    .notification("Subscription Expiry Notification Created")
                    .websocket("Company can not receive notification through Websocket")
                    .build();
        }

        return NotificationResponseMessageDto.builder()
                .notification("Subscription Expiry Notification Created")
                .websocket("Company received notification through Websocket")
                .build();
    }
}
