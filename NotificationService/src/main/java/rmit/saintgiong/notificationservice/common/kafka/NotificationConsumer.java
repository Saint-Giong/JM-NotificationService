package rmit.saintgiong.notificationservice.common.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import rmit.saintgiong.notificationapi.common.dto.request.CreateNotificationRequest;
import rmit.saintgiong.notificationapi.common.type.KafkaTopic;
import rmit.saintgiong.notificationapi.services.InternalCreateNotificationInterface;
import rmit.saintgiong.notificationservice.avro.ApplicantNotificationAction;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final InternalCreateNotificationInterface createNotificationService;

    @KafkaListener(topics = KafkaTopic.NEW_APPLICANT_TOPIC, containerFactory = "notificationKafkaListenerContainerFactory")
    @SendTo(KafkaTopic.NEW_APPLICANT_REPLY_TOPIC)
    public String consumeNewApplicant(ApplicantNotificationAction message) {
        log.info("Received new applicant notification for company: {}", message.getCompanyId());
        
        createNotificationService.createNotification(
                CreateNotificationRequest.builder()
                        .companyId(UUID.fromString(message.getCompanyId().toString()))
                        .applicantId(message.getApplicantId() != null ? message.getApplicantId().toString() : "placeholder-id")
                        .title("New Applicant")
                        .message("New Applicant ID: " + message.getApplicantId())
                        .build()
        );
        return "Notification Created";
    }

    @KafkaListener(topics = KafkaTopic.EDIT_APPLICANT_TOPIC, containerFactory = "notificationKafkaListenerContainerFactory")
    @SendTo(KafkaTopic.EDIT_APPLICANT_REPLY_TOPIC)
    public String consumeEditApplicant(ApplicantNotificationAction message) {
        log.info("Received edit applicant notification for company: {}", message.getCompanyId());

        createNotificationService.createNotification(
                CreateNotificationRequest.builder()
                        .companyId(UUID.fromString(message.getCompanyId().toString()))
                        .applicantId(message.getApplicantId() != null ? message.getApplicantId().toString() : "placeholder-id")
                        .title("Applicant Updated")
                        .message("Applicant (ID: " + message.getApplicantId() + ") has updated their application.")
                        .build()
        );
        return "Notification Created";
    }
}
