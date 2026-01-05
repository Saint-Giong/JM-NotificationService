package rmit.saintgiong.jmnotificationapi.internal.common.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNotificationRequest {
    @Schema(description = "Updated title of the notification", example = "Updated Job Application Status")
    private String title;

    @Schema(description = "Updated message content", example = "The status of your application has been updated.")
    private String message;

    @Schema(description = "Read status of the notification", example = "true")
    private Boolean isRead;
}
