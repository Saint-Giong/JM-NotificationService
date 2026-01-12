package rmit.saintgiong.jmnotificationapi.internal.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseMessageDto {
    private String notification;
    private String websocket;
}
