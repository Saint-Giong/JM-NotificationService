package rmit.saintgiong.notificationapi.common.dto.request;

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
public class CreateNotificationRequest {

    @Schema(description = "ID of the company", example = "uuid-placeholder")
    private UUID companyId;

    @Schema(description = "ID of the applicant", example = "uuid-placeholder")
    private String applicantId;

    @Schema(description = "ID of the job post", example = "uuid-placeholder")
    private String jobPostId;

    @Schema(description = "Title of the notification", example = "New Applicant")
    private String title;

    @Schema(description = "Message content", example = "You have a new applicant")
    private String message;
}
