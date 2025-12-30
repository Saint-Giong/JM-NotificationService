package rmit.saintgiong.notificationservice.domain.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import rmit.saintgiong.notificationapi.common.dto.request.NotificationDto;
import rmit.saintgiong.notificationapi.common.dto.request.UpdateNotificationRequest;
import rmit.saintgiong.notificationapi.common.dto.response.NotificationResponseDto;
import rmit.saintgiong.notificationservice.domain.entity.CompanyNotificationEntity;

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
    @Mapping(source = "isRead", target = "read")
    void updateEntityFromRequest(UpdateNotificationRequest request, @MappingTarget CompanyNotificationEntity entity);

    @Mapping(source = "read", target = "isRead")
    NotificationResponseDto toResponse(CompanyNotificationEntity entity);
}
