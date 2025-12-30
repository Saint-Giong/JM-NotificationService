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
public class CreateApplicantNotificationRequest {

    @Schema(description = "ID of the company", example = "2a261683-54b8-4365-8775-0d6be8a9c493")
    private UUID companyId;

    @Schema(description = "ID of the applicant", example = "947491ba-2a3b-4a52-ac34-ccb60f586db9")
    private UUID applicantId;
}
