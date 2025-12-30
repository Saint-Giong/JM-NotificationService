package rmit.saintgiong.notificationapi.common.type;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class KafkaTopic {
    public static final String NEW_APPLICANT_TOPIC = "JM_NEW_APPLICANT_REQUEST";
    public static final String EDIT_APPLICANT_TOPIC = "JM_EDIT_APPLICANT_REQUEST";
    public static final String NEW_APPLICANT_REPLY_TOPIC = "JM_NEW_APPLICANT_REPLIED";
    public static final String EDIT_APPLICANT_REPLY_TOPIC = "JM_EDIT_APPLICANT_REPLIED";
}
