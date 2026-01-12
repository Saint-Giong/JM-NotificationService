package rmit.saintgiong.jmnotificationapi.internal.common.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationBuilderDto {

    @Schema(description = "ID of the company", example = "uuid-placeholder")
    private UUID companyId;

    @Schema(description = "ID of the applicant", example = "uuid-placeholder")
    private UUID applicantId;

    @Schema(description = "Title of the notification", example = "New Applicant")
    private String title;

    @Schema(description = "Message content", example = "You have a new applicant")
    private String message;

    @Schema(description = "Initial read status", example = "false")
    private boolean isRead;
}
