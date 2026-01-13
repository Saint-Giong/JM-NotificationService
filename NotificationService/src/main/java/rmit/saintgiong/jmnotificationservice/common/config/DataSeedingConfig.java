package rmit.saintgiong.jmnotificationservice.common.config;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rmit.saintgiong.jmnotificationservice.domain.entity.CompanyNotificationEntity;
import rmit.saintgiong.jmnotificationservice.domain.repository.CompanyNotificationRepository;




@Component
@Slf4j
@RequiredArgsConstructor
public class DataSeedingConfig implements CommandLineRunner {

    private final CompanyNotificationRepository companyNotificationRepository;

    // Company UUIDs - must match Auth service
    private static final UUID NAB_COMPANY_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID GOOGLE_COMPANY_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID NETCOMPANY_COMPANY_ID = UUID.fromString("33333333-3333-3333-3333-333333333333");
    private static final UUID SHOPEE_COMPANY_ID = UUID.fromString("44444444-4444-4444-4444-444444444444");

    // Transaction UUIDs - for reference in notification messages
    private static final UUID NAB_TRANSACTION_ID = UUID.fromString("11111111-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
    private static final UUID GOOGLE_TRANSACTION_ID = UUID.fromString("22222222-bbbb-bbbb-bbbb-bbbbbbbbbbbb");
    private static final UUID NETCOMPANY_TRANSACTION_ID = UUID.fromString("33333333-cccc-cccc-cccc-cccccccccccc");
    private static final UUID SHOPEE_TRANSACTION_ID = UUID.fromString("44444444-dddd-dddd-dddd-dddddddddddd");

    // Subscription price
    private static final Double SUBSCRIPTION_PRICE = 29.99;

    @Override
    public void run(String @NonNull ... args) {
        if (companyNotificationRepository.count() != 0) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();

        List<CompanyNotificationEntity> seeds = List.of(
                // ============================================================
                // FREEMIUM 1: NAB - EXPIRED subscription (expiry: now - 31 days)
                // Notifications:
                // 1. Payment successful (when subscription was purchased: now - 61 days)
                // 2. Subscription expired (when subscription expired: now - 31 days)
                // All marked as READ
                // ============================================================
                
                // NAB: Payment Successful Notification
                CompanyNotificationEntity.builder()
                        .title("Payment Successful")
                        .message(String.format(
                                "Your payment of $%.2f USD has been processed successfully. " +
                                "Transaction ID: %s. Your Premium subscription is now active for 30 days. " +
                                "Thank you for your purchase!",
                                SUBSCRIPTION_PRICE,
                                "txn_nab_" + NAB_TRANSACTION_ID.toString().substring(0, 8)
                        ))
                        .isRead(true)
                        .createdAt(now.minusDays(61)) // When subscription was purchased
                        .companyId(NAB_COMPANY_ID)
                        .build(),

                // NAB: Subscription Expired Notification
                CompanyNotificationEntity.builder()
                        .title("Subscription Expired")
                        .message(
                                "Your Premium subscription has expired. " +
                                "You have been downgraded to Freemium tier. " +
                                "To continue enjoying Premium features including unlimited job posts and talent discovery, " +
                                "please renew your subscription."
                        )
                        .isRead(true)
                        .createdAt(now.minusDays(31)) // When subscription expired
                        .companyId(NAB_COMPANY_ID)
                        .build(),

                // ============================================================
                // FREEMIUM 2: Google - CANCELLED subscription (expiry: null)
                // Transaction was 50 days ago
                // Logic: if EXPIRED > 3 days, set status as CANCELLED and set expiry to null
                // Notifications:
                // 1. Payment successful (when subscription was purchased: now - 50 days)
                // 2. Subscription expired (when subscription expired: now - 20 days, as 50-30=20)
                // All marked as READ
                // ============================================================
                
                // Google: Payment Successful Notification
                CompanyNotificationEntity.builder()
                        .title("Payment Successful")
                        .message(String.format(
                                "Your payment of $%.2f USD has been processed successfully. " +
                                "Transaction ID: %s. Your Premium subscription is now active for 30 days. " +
                                "Thank you for choosing our Premium plan!",
                                SUBSCRIPTION_PRICE,
                                "txn_google_" + GOOGLE_TRANSACTION_ID.toString().substring(0, 8)
                        ))
                        .isRead(true)
                        .createdAt(now.minusDays(50)) // When subscription was purchased (50 days ago)
                        .companyId(GOOGLE_COMPANY_ID)
                        .build(),

                // Google: Subscription Expired Notification
                CompanyNotificationEntity.builder()
                        .title("Subscription Expired")
                        .message(
                                "Your Premium subscription has expired. " +
                                "You have been downgraded to Freemium tier. " +
                                "To continue enjoying Premium features including unlimited job posts and talent discovery, " +
                                "please renew your subscription."
                        )
                        .isRead(true)
                        .createdAt(now.minusDays(20)) // When subscription expired (50-30=20 days ago)
                        .companyId(GOOGLE_COMPANY_ID)
                        .build(),

                // ============================================================
                // PREMIUM 1: Netcompany - ACTIVE subscription (expiry: now + 30 days)
                // Notifications:
                // 1. Payment successful (when subscription was purchased: now)
                // All marked as READ
                // ============================================================
                
                // Netcompany: Payment Successful Notification
                CompanyNotificationEntity.builder()
                        .title("Payment Successful")
                        .message(String.format(
                                "Your payment of $%.2f USD has been processed successfully. " +
                                "Transaction ID: %s. Your Premium subscription is now active for 30 days. " +
                                "Enjoy unlimited job posts and talent discovery features!",
                                SUBSCRIPTION_PRICE,
                                "txn_netcompany_" + NETCOMPANY_TRANSACTION_ID.toString().substring(0, 8)
                        ))
                        .isRead(true)
                        .createdAt(now) // Just purchased
                        .companyId(NETCOMPANY_COMPANY_ID)
                        .build(),

                // ============================================================
                // PREMIUM 2: Shopee - ACTIVE subscription (expiry: now + 30 days)
                // Notifications:
                // 1. Payment successful (when subscription was purchased: now)
                // All marked as READ
                // ============================================================
                
                // Shopee: Payment Successful Notification
                CompanyNotificationEntity.builder()
                        .title("Payment Successful")
                        .message(String.format(
                                "Your payment of $%.2f USD has been processed successfully. " +
                                "Transaction ID: %s. Your Premium subscription is now active for 30 days. " +
                                "Enjoy unlimited job posts and talent discovery features!",
                                SUBSCRIPTION_PRICE,
                                "txn_shopee_" + SHOPEE_TRANSACTION_ID.toString().substring(0, 8)
                        ))
                        .isRead(true)
                        .createdAt(now) // Just purchased
                        .companyId(SHOPEE_COMPANY_ID)
                        .build()
        );

        companyNotificationRepository.saveAll(seeds);
        log.info("Seeded {} notifications: 2 Freemiums (payment + expired) + 2 Premiums (payment success), all READ.", seeds.size());
    }
}
