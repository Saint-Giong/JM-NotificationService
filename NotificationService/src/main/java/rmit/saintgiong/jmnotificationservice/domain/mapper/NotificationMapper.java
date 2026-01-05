package rmit.saintgiong.jmnotificationservice.domain.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import rmit.saintgiong.jmnotificationapi.internal.common.dto.request.NotificationDto;
import rmit.saintgiong.jmnotificationapi.internal.common.dto.request.UpdateNotificationRequest;
import rmit.saintgiong.jmnotificationapi.internal.common.dto.response.NotificationResponseDto;
import rmit.saintgiong.jmnotificationservice.domain.entity.CompanyNotificationEntity;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(target = "notificationId", ignore = true)
    @Mapping(target = "isRead", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    CompanyNotificationEntity toEntity(NotificationDto request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "notificationId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "companyId", ignore = true)
    @Mapping(source = "isRead", target = "isRead")
    void updateEntityFromRequest(UpdateNotificationRequest request, @MappingTarget CompanyNotificationEntity entity);

    @Mapping(source = "isRead", target = "isRead")
    NotificationResponseDto toResponse(CompanyNotificationEntity entity);
}
