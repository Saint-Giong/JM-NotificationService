package rmit.saintgiong.notificationservice.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmit.saintgiong.notificationservice.domain.entity.CompanyNotification;

import java.util.UUID;

@Repository
public interface CompanyNotificationRepository extends JpaRepository<CompanyNotification, UUID> {
    Page<CompanyNotification> findByCompanyId(UUID companyId, Pageable pageable);
}
