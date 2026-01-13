# JM-NotificationService

Real-time notification service for the Job Manager platform.

## Overview

The Notification Service manages all notification delivery for companies, including payment confirmations, subscription expiry alerts, and system notifications. It supports both WebSocket (real-time) and REST (historical) delivery methods.

## Features

- **Real-Time Notifications**: WebSocket-based push notifications
- **Notification History**: Store and retrieve past notifications
- **Read/Unread Status**: Track notification read status
- **Event-Driven**: Kafka integration for automatic notifications
- **Notification Types**: Payment, subscription, system alerts
- **WebSocket Broadcasting**: Instant delivery to connected clients
- **Pagination Support**: Efficient historical notification retrieval

## Tech Stack

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data JPA**: Database persistence
- **PostgreSQL**: Notifications database
- **Spring WebSocket**: Real-time communication
- **STOMP Protocol**: WebSocket messaging
- **Kafka**: Event-driven notification triggers
- **Lombok**: Reduce boilerplate code
- **JWE**: Secure authentication

## Prerequisites

- Java 17 or higher
- Maven 3.8+
- PostgreSQL database
- Kafka broker
- WebSocket-compatible client

## Database Schema

### Table: `company_notifications`

| Column            | Type      | Description                              |
| ----------------- | --------- | ---------------------------------------- |
| `notification_id` | UUID (PK) | Notification identifier (auto-generated) |
| `company_id`      | UUID      | Company ID (FK to Auth service)          |
| `title`           | VARCHAR   | Notification title                       |
| `message`         | TEXT      | Notification message content             |
| `is_read`         | BOOLEAN   | Read status (true/false)                 |
| `created_at`      | TIMESTAMP | Auto-generated timestamp                 |

## Data Seeding

The service automatically seeds **6 notifications** for 4 companies:

### Freemium Companies (4 notifications total)

**NAB - EXPIRED Subscription (2 notifications)**

1. **Payment Successful** (61 days ago, READ)

   ```
   Title: Payment Successful
   Message: Your payment of $29.99 USD has been processed successfully.
            Transaction ID: txn_nab_11111111
   ```

2. **Subscription Expired** (31 days ago, READ)
   ```
   Title: Subscription Expired
   Message: Your Premium subscription has expired.
            You have been downgraded to Freemium tier.
   ```

**Google - CANCELLED Subscription (2 notifications)**

1. **Payment Successful** (50 days ago, READ)

   ```
   Title: Payment Successful
   Message: Your payment of $29.99 USD has been processed successfully.
            Transaction ID: txn_google_22222222
   ```

2. **Subscription Expired** (20 days ago, READ)
   ```
   Title: Subscription Expired
   Message: Your Premium subscription has expired.
            You have been downgraded to Freemium tier.
   ```

### Premium Companies (2 notifications total)

**Netcompany - ACTIVE Subscription (1 notification)**

1. **Payment Successful** (just now, READ)
   ```
   Title: Payment Successful
   Message: Your payment of $29.99 USD has been processed successfully.
            Transaction ID: txn_netcompany_33333333
            Enjoy unlimited job posts and talent discovery features!
   ```

**Shopee - ACTIVE Subscription (1 notification)**

1. **Payment Successful** (just now, READ)
   ```
   Title: Payment Successful
   Message: Your payment of $29.99 USD has been processed successfully.
            Transaction ID: txn_shopee_44444444
            Enjoy unlimited job posts and talent discovery features!
   ```

> **Summary**: 2 Freemiums (payment + expired) + 2 Premiums (payment success), all marked as READ.

## WebSocket Connection

### WebSocket Endpoint

```
ws://host:port/ws-notifications
```

### Subscribe To

```
/topic/notifications/{companyId}
```

## Notification Types

### 1. Payment Notifications

- Payment Successful
- Payment Failed
- Payment Refunded

### 2. Subscription Notifications

- Subscription Activated
- Subscription Expired
- Subscription Cancelled
- Subscription Renewed

### 3. Job Post Notifications

- Job Post Expired
- Job Post Published
- Application Received

### 4. System Notifications

- System Maintenance
- New Features
- Security Alerts
