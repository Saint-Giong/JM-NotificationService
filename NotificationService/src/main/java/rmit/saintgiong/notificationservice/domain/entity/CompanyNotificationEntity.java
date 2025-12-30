package rmit.saintgiong.notificationservice.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "company_notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
@EntityListeners(AuditingEntityListener.class)
public class CompanyNotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID notificationId;

    private String title;

    private String message;

    private boolean isRead;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    private UUID companyId;

}
