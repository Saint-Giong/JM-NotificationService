package rmit.saintgiong.jmnotificationservice.domain.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.web.bind.annotation.*;
import rmit.saintgiong.jmnotificationapi.internal.common.dto.request.CreateKafkaNotificationRequestDto;
import rmit.saintgiong.jmnotificationapi.internal.common.dto.response.NotificationResponseDto;
import rmit.saintgiong.jmnotificationapi.internal.services.InternalCreateNotificationInterface;
import rmit.saintgiong.jmnotificationapi.internal.services.InternalDeleteNotificationInterface;
import rmit.saintgiong.jmnotificationapi.internal.services.InternalGetNotificationInterface;
import rmit.saintgiong.jmnotificationapi.internal.services.InternalUpdateNotificationInterface;
import rmit.saintgiong.jmnotificationapi.external.common.dto.avro.ApplicantNotificationAction;
import rmit.saintgiong.shared.type.KafkaTopic;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

@RestController
@RequiredArgsConstructor
@Tag(name = "Notification Management", description = "APIs for managing company notifications")
public class NotificationController {

    private final ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate;
    private final InternalCreateNotificationInterface createService;
    private final InternalGetNotificationInterface getService;
    private final InternalUpdateNotificationInterface updateService;
    private final InternalDeleteNotificationInterface deleteService;

    @Operation(summary = "Test create a new notification")
    @ApiResponse(responseCode = "201", description = "Notification request sent successfully")
    @PostMapping("/")
    public Callable<ResponseEntity<String>> createNotification(
            @RequestBody CreateKafkaNotificationRequestDto request,
            @RequestParam(defaultValue = "NEW") String type) {
        return () -> {
            ApplicantNotificationAction avroMessage = new ApplicantNotificationAction(
                    request.getCompanyId(),
                    request.getApplicantId()
            );

            String topic;
            if ("UPDATE".equalsIgnoreCase(type)) {
                topic = KafkaTopic.EDIT_APPLICANT_TOPIC_REQUEST;
            } else {
                topic = KafkaTopic.NEW_APPLICANT_TOPIC_REQUEST;
            }

            ProducerRecord<String, Object> record = new ProducerRecord<>(topic, avroMessage);
            RequestReplyFuture<String, Object, Object> replyFuture = replyingKafkaTemplate.sendAndReceive(record);
            
            // Wait for reply
            ConsumerRecord<String, Object> consumerRecord = replyFuture.get();
            String replyMessage = (String) consumerRecord.value();

            return ResponseEntity.status(HttpStatus.CREATED).body("Request sent to Kafka topic: " + topic + ". Reply: " + replyMessage + ". Applicant ID: " + avroMessage.getApplicantId());
        };
    }

    @Operation(summary = "Get a notification by ID")
    @ApiResponse(responseCode = "200", description = "Notification found")
    @ApiResponse(responseCode = "404", description = "Notification not found")
    @GetMapping("/{id}")
    public Callable<ResponseEntity<NotificationResponseDto>> getNotificationById(@PathVariable UUID id) {
        return () -> ResponseEntity.ok(getService.getNotificationById(id));
    }

    @Operation(summary = "Get all notifications")
    @ApiResponse(responseCode = "200", description = "List of all notifications")
    @GetMapping("/")
    public Callable<ResponseEntity<List<NotificationResponseDto>>> getAllNotifications() {
        return () -> ResponseEntity.ok(getService.getAllNotifications());
    }

    @Operation(summary = "Get notifications by company ID with pagination")
    @ApiResponse(responseCode = "200", description = "Paginated list of notifications")
    @GetMapping("/company/{companyId}")
    public Callable<ResponseEntity<Page<NotificationResponseDto>>> getNotificationsByCompanyId(
            @PathVariable UUID companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return () -> ResponseEntity.ok(getService.getNotificationsByCompanyId(companyId, pageable));
    }

    @Operation(summary = "Mark a notification as read")
    @ApiResponse(responseCode = "200", description = "Notification marked as read")
    @ApiResponse(responseCode = "404", description = "Notification not found")
    @PatchMapping("/{id}/read")
    public Callable<ResponseEntity<NotificationResponseDto>> markAsRead(@PathVariable UUID id) {
        return () -> ResponseEntity.ok(updateService.updateNotificationIsRead(id));
    }

    @Operation(summary = "Delete a notification")
    @ApiResponse(responseCode = "204", description = "Notification deleted successfully")
    @ApiResponse(responseCode = "404", description = "Notification not found")
    @DeleteMapping("/{id}")
    public Callable<ResponseEntity<Void>> deleteNotification(@PathVariable UUID id) {
        return () -> {
            deleteService.deleteNotification(id);
            return ResponseEntity.noContent().build();
        };
    }
}
