package rmit.saintgiong.jmnotificationapi.internal.common.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class GenericResponseDto<T> {
    private boolean success;
    private String message;
    private T data;
}