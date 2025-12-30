package rmit.saintgiong.notificationapi.common.dto;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
public class GenericResponseDto<T> {
    private boolean success;
    private String message;
    private final T data;
}