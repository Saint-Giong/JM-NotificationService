package rmit.saintgiong.jmnotificationapi.internal.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {
    private UUID notificationId;
    private String title;
    private String message;
    private Boolean isRead;
    private LocalDateTime createdAt;
    private UUID companyId;
}
