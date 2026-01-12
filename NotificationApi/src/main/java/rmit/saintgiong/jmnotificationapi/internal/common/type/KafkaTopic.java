package rmit.saintgiong.jmnotificationapi.internal.common.type;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class KafkaTopic {
    public static final String COMPANY_REGISTRATION_REQUEST_TOPIC = "JM_COMPANY_REGISTRATION";
    public static final String COMPANY_REGISTRATION_REPLY_TOPIC = "JM_COMPANY_REGISTRATION_REPLIED";

    public static final String CREATE_SUBSCRIPTION_REQUEST_TOPIC = "JM_CREATE_SUBSCRIPTION_REQUEST";
    public static final String CREATE_SUBSCRIPTION_RESPONSE_TOPIC = "JM_CREATE_SUBSCRIPTION_RESPONSE";

    public static final String MAIL_SUBSCRIPTION_SENT_REQUEST_TOPIC = "JM_MAIL_SUBSCRIPTION_SENT_REQUEST";

    public static final String SUBSCRIPTION_PAID_NOTIFICATION_TOPIC = "JM_SUBSCRIPTION_PAID_NOTIFICATION";

    public static final String SUBSCRIPTION_EXPIRY_NOTIFICATION_TOPIC = "JM_SUBSCRIPTION_EXPIRY_NOTIFICATION_REQUEST";
}
