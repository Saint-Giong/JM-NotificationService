package rmit.saintgiong.jmnotificationservice.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmit.saintgiong.jmnotificationservice.domain.entity.CompanyNotificationEntity;

import java.util.UUID;

@Repository
public interface CompanyNotificationRepository extends JpaRepository<CompanyNotificationEntity, UUID> {
    Page<CompanyNotificationEntity> findByCompanyId(UUID companyId, Pageable pageable);
}
