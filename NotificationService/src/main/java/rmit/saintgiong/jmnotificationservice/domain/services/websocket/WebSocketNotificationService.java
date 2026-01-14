package rmit.saintgiong.jmnotificationservice.domain.services.websocket;

import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rmit.saintgiong.jmnotificationapi.internal.common.dto.response.NotificationResponseDto;
import rmit.saintgiong.jmnotificationservice.common.utils.JweTokenService;
import rmit.saintgiong.shared.token.TokenClaimsDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketNotificationService {

    private final SocketIOServer socketIOServer;
    private final JweTokenService jweTokenService;

    @PostConstruct
    public void start() {

        socketIOServer.addConnectListener(client -> {

            String token = extractToken(client);
            if (token != null) {
                try {
                    TokenClaimsDto claims = jweTokenService.validateAccessToken(token);
                    UUID userId = claims.getSub();
                    // Assuming sub is the companyId/userId
                    client.joinRoom("company_" + userId.toString());
                    log.info("Client {} joined room company_{}", client.getSessionId(), userId);
                } catch (Exception e) {
                    log.warn("Invalid token for client {}: {}", client.getSessionId(), e.getMessage());
                }
            } else {
                log.info("No token found for client");
                String id = client.getHandshakeData().getSingleUrlParam("token");
                log.info("client id: {}", id);

                if (id != null && !id.isEmpty()) {
                    client.joinRoom("company_" + id);

                } else {
                    log.info("No token found in query params for client {}", client.getSessionId());
                }
            }
        });

        socketIOServer.addEventListener("notification:read", Map.class, (client, data, ackSender) -> {
            log.info("Notification read event received: {}", data);
            // Implement further logic if needed (e.g. mark as read in DB)
        });

        log.info("Start Server WebSocketNotificationService...");
        socketIOServer.start();
    }

    @PreDestroy
    public void stop() {
        if (socketIOServer != null) {
            socketIOServer.stop();
        }
    }

    public void sendNotification(UUID companyId, NotificationResponseDto notification) {
        log.info("Sending WebSocket notification to company: {}", companyId);
        socketIOServer.getRoomOperations("company_" + companyId)
                .sendEvent("notification", notification);
    }

    private String extractToken(SocketIOClient client) {

        String authHeader = client.getHandshakeData().getHttpHeaders().get("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        String cookieHeader = client.getHandshakeData().getHttpHeaders().get("Cookie");
        if (cookieHeader != null) {
            String[] cookies = cookieHeader.split(";");
            for (String cookie : cookies) {
                String[] parts = cookie.trim().split("=");
                if (parts.length == 2) {
                    // Check for common auth cookie names
                    if ("access_token".equals(parts[0]) || "auth_token".equals(parts[0]) || "token".equals(parts[0])) {
                        return parts[1];
                    }
                    // Also check for standard Java/Spring cookie names if generic
                    if ("JSESSIONID".equals(parts[0])) {
                        // Usually not the JWT but session id.
                    }
                }
            }
        }
        return null;
    }
}
